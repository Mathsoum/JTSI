#include "stdafx.h"
// jtsi.cpp : Defines the exported functions for the DLL application.
//
#include <jni.h>
#include <fstream>
#include <iostream>
#include <cstring>
#include "trnsys.h" //TRNSYS acess functions (allow to acess TIME etc.)


// The environment variable's name must be of wide char type
#define ENV_VAR_JAR_LOCATION	"JTSI_JAR"
#define	NB_ITEMS				3
#define LOADEDCLASS				"jtsi/TrnsysInterface"

void printLog(std::string str) {
	std::ofstream file("E:\\ter\\out.log", std::ios::app);
	file.write(str.data(), str.length());
	file << std::endl;
	file.close();
}

extern "C" __declspec(dllexport)
int TYPE9002 (
				double &time,  // the simulation time
				double xin[],  // the array containing the component InpUTS
				double xout[], // the array which the component fills with its appropriate OUTPUTS
				double &t,     // the array containing the dependent variables for which the derivatives are evaluated 
				double &dtdt,  // the array containing the derivatives of T which are evaluated 
				double par[],  // the array containing the PARAMETERS of the component
				int info[],    // the information array described in Section 3.3.3 of the manual (7.4.3 Info Array new version)
				int icntrl     // the control array described in Section 3.3.4 of the manual
		)
{
	//SET THE VERSION INFORMATION FOR TRNSYS
	if (info[6]== -2) 
	{
		info[11]=16;
		return 1;
	}

	//DO ALL THE VERY LAST CALL OF THE SIMULATION MANIPULATIONS HERE
	if (info[7]== -1)
	{
		// Stopping JVM
		//int nitems=NB_ITEMS;
		//double stored[NB_ITEMS];
		//GETSTORAGEVARS(stored, &nitems, info);
		//((JavaVM*)(int)stored[2])->DetachCurrentThread();
		return 1;
	}

	//maybe : if the unit has converged then return 1
	if (info[12]>0)
	{
		return 1;
	}

	//DO ALL THE VERY FIRST CALL OF THE SIMULATION MANIPULATIONS HERE
	if (info[6]== -1) // first call of this component in the simulation
	{
		info[5]=1;
		info[8]=1;
		info[9]=0;	// STORAGE FOR VERSION 16 HAS BEEN CHANGED

		int nitems = NB_ITEMS;
		SETSTORAGESIZE(&nitems,info);
		return 1;
	}

	//    DO ALL OF THE INITIAL TIMESTEP MANIPULATIONS HERE - THERE ARE NO ITERATIONS AT THE INTIAL TIME
	if (time < (getSimulationStartTime()+getSimulationTimeStep()/2.0)) 
	{
		char* jarLocation = new char[256];
		GetEnvironmentVariableA(ENV_VAR_JAR_LOCATION, jarLocation, 256);
		char* optionString = new char[512];
		std::strcpy(optionString, "-Djava.class.path=");
		std::strcat(optionString, jarLocation);

		double* stored = new double[NB_ITEMS];
		int nitems = NB_ITEMS;
		JavaVM* jvm = NULL;
		JNIEnv* env = NULL;
		JavaVMInitArgs args;
		JavaVMOption* options = new JavaVMOption;
		args.version = JNI_VERSION_1_2;
		args.nOptions = 1;
		options->optionString = optionString;
		args.options = options;
		args.ignoreUnrecognized = JNI_TRUE;
		int res = JNI_CreateJavaVM(&jvm, (void **)&env, &args);
		char* tmp = new char[3];
		jclass loadedClass = env->FindClass(LOADEDCLASS);

		jmethodID init = env->GetMethodID(loadedClass,"<init>","(I)V");
		jobject jo = env->NewObject(loadedClass, init, (int)par[2]);
		
		stored[0] = (int)env;
		stored[1] = (int)jo;
		stored[2] = (int)jvm;

		//Envoi de info et parameter
		jboolean isCopy;
		jintArray jdi = env->NewIntArray(10);
		jint* destArrayElems = env->GetIntArrayElements(jdi, &isCopy);
		for (int i = 0; i < 10; i++) {
			destArrayElems[i] = info[i];
		} 

		if (isCopy == JNI_TRUE) {
			env->ReleaseIntArrayElements(jdi, destArrayElems, 0);
		}
		jmethodID setInfo = env->GetMethodID(loadedClass,"setInfo","([I)V");
		env->CallVoidMethod(jo, setInfo, jdi);

		jdoubleArray jda = env->NewDoubleArray(info[3]);
		jdouble* destArrayElemsD = env->GetDoubleArrayElements(jda, &isCopy);
		for (int i = 0; i < info[3]; i++) {
			destArrayElemsD[i] = par[i];
		} 

		if (isCopy == JNI_TRUE) {
			env -> ReleaseDoubleArrayElements(jda, destArrayElemsD, 0);
		}
		jmethodID setPar = env->GetMethodID(loadedClass,"setParameters","([D)V");
		env->CallVoidMethod(jo, setPar, jda);
		SETSTORAGEVARS(stored, &nitems, info);
		delete jarLocation;
		delete optionString;
		delete options;
		delete stored;

		return 1;
	} // END INITIAL TIMESTEP MANIPULATIONS

	int nitems=NB_ITEMS;
	double stored[NB_ITEMS];
	GETSTORAGEVARS(stored, &nitems, info);
	xout[0] = 0;
	if (info[6] == 0) {
		int addrje = (int)stored[0];
		jboolean isCopy;
		jobject jo = (jobject)(int)stored[1];
		JNIEnv *je = (JNIEnv*)addrje;
		jclass loadedClass = je->FindClass(LOADEDCLASS);
		jdoubleArray jda = je->NewDoubleArray(info[2]);
		jdouble* destArrayElems = je->GetDoubleArrayElements(jda, &isCopy);
		for (int i = 0; i < info[2]; i++) {
			destArrayElems[i] = xin[i];
		} 

		if (isCopy == JNI_TRUE) {
			je -> ReleaseDoubleArrayElements(jda, destArrayElems, 0);
		}
		jmethodID setXin = je->GetMethodID(loadedClass,"setXin","([D)V");
		je->CallVoidMethod(jo, setXin, jda);
		//delete jda;
		//delete setXin;

		jmethodID setTimestep = je->GetMethodID(loadedClass,"setTimestep","(I)V");
		
		je->CallVoidMethod(jo, setTimestep, (jint)time);
		//delete setTimestep;
		jmethodID step = je->GetMethodID(loadedClass,"step","()V");
		je->CallIntMethod(jo, step);
		//delete step;
		
		jmethodID getXout = je->GetMethodID(loadedClass,"getXout","()[D");

		jobject temp = je->CallObjectMethod(jo, getXout);
		jdoubleArray* jtab = reinterpret_cast<jdoubleArray*>(&temp);


		double *tab= (jdouble*) je->GetDoubleArrayElements(*jtab, NULL);
		jsize arrayLength = je->GetArrayLength(*jtab);
		for (int i=0;i<arrayLength;i++) {
			xout[i] = tab[i];
		}
		je->ReleaseDoubleArrayElements(*jtab, tab, 0);
		//delete getXout;
		//delete temp;
		//delete jtab;
	}
	return 1;
}