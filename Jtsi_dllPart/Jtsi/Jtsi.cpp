#include "stdafx.h"
// jtsi.cpp : Defines the exported functions for the DLL application.
//
#include <jni.h>
#include <fstream>
#include <iostream>
#include "trnsys.h" //TRNSYS acess functions (allow to acess TIME etc.)


//#define	ADDR_JAVA	"-Djava.class.path=..\\..\\UserLib\\ReleaseDLLs\\jtsijavapart.jar"
#define	ADDR_JAVA	"-Djava.class.path=E:\\ter\\sourceRepos\\JTSI\\jtsijavapart.jar"
#define	NB_ITEMS	3
#define LOADEDCLASS "jtsi/TrnsysInterface"

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
//* Java part
//set output nb
	info[5] = 1; //(int)par[1];
	//std::string log("Output count = ");
	//char* var = new char[2];
	//sprintf(var, "%d", info[5]);
	//log += var;
	//prinLog(log);
	//delete var;

	int iunit; // UNIT number ('serial number' of the component, from the input file (the 'deck')
	int itype; // TYPE number (component number)

//	PARAMETERS
	double nbInputs;
	double nbOutputs;
	double uniqueId;
	double nbParameters;
	double parameter;

//	INPUTS
	double input;

	nbInputs=par[0];
	nbOutputs=par[1];
	uniqueId=par[2];
	nbParameters=par[3];
	parameter=par[4];

	input=xin[0];
	iunit=info[0];
	itype=info[1];
	
	info[8] = 0;
	info[9] = 0;

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
		double* stored = new double[NB_ITEMS];
		int nitems = NB_ITEMS;

		// Old
		//JavaVM* jvm;
		//JNIEnv* env;
		//JavaVMInitArgs args;

		//JavaVMOption* options = new JavaVMOption;
		//args.version = JNI_VERSION_1_4;
		//args.nOptions = 1;
		//options->optionString = ADDR_JAVA;
		//args.options = options;
		//args.ignoreUnrecognized = JNI_TRUE;

		// New
		JavaVM* jvm = NULL;
		JNIEnv* env = NULL;
		JavaVMInitArgs args;
		JavaVMOption* options = new JavaVMOption;
		args.version = JNI_VERSION_1_2;
		args.nOptions = 1;
		options->optionString = ADDR_JAVA;
		args.options = options;
		args.ignoreUnrecognized = JNI_TRUE;

		//FIXME Impossible de créer la JVM
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
/*
//-----------------------------------------------------------------------------------------------------------------------

  // *** STANDARD TRNSYS DECLARATIONS
  int npar= 5;   // number of parameters we expect
  int nin= 1;   // number of inputs
  int nout=1; // number of outputs
  int nder=0;   // number of derivatives
  int iunit; // UNIT number ('serial number' of the component, from the input file (the 'deck')
  int itype; // TYPE number (component number) 
  // read context information from TRNSYS
  // (uncomment lines as required)
	info[5] = nout;  // number of outputs 

	iunit = info[0]; // UNIT number
	itype = info[1]; // TYPE number

	//info[2]	; // number of INPUTS specified by the user of the component 
	//info[3]	; // number of PARAMETERS specified by the user of the component
	//info[4]	; // number of DERIVATIVES specified by the user of the component
	//info[5]	; // number of OUTPUTS specified by the user of the component

  //info[6]	; // number of iterative calls to the UNIT in the current timestep
              // -2 = initialization
              // -1	= initial call in simulation for this UNIT
	            //  0 = first call in timestep for this UNIT.
              //  1	= second call in timestep for this UNIT, etc.

	//info(7)	; // total number of calls to the UNIT in the simulation
  // *** inform TRNSYS about properties of this type
  info[8] = 0; // indicates whether TYPE depends on the passage of time: 0=no
	info[9]	= 0; //	use to allocate storage (see Section 3.5 of the TRNSYS manual): 0 = none
	// info[10]; // indicates number of discrete control variables (see Section 3.3.4 of the TRNSYS manual)
//-----------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------
//    ADD DECLARATIONS AND DEFINITIONS FOR THE USER-VARIABLES HERE

//-----------------------------------------------------------------------------------------------------------------------

//    PARAMETERS
      double nbInputs;
      double nbOutputs;
      double uniqueId;
      double nbParameters;
      double parameter;

//    INPUTS
      double input;

//-----------------------------------------------------------------------------------------------------------------------
//       READ IN THE VALUES OF THE PARAMETERS IN SEQUENTIAL ORDER
      nbInputs=par[0];
      nbOutputs=par[1];
      uniqueId=par[2];
      nbParameters=par[3];
      parameter=par[4];

//-----------------------------------------------------------------------------------------------------------------------
//    RETRIEVE THE CURRENT VALUES OF THE INPUTS TO THIS MODEL FROM THE XIN ARRAY IN SEQUENTIAL ORDER

      input=xin[0];
	 iunit=info[0];
	 itype=info[1];

//-----------------------------------------------------------------------------------------------------------------------
//    SET THE VERSION INFORMATION FOR TRNSYS
      if (info[6]== -2) 
    {
	   info[11]=16;
     // add additional initialisation code here, if any
	   return 1;
    }
//-----------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------
//    DO ALL THE VERY LAST CALL OF THE SIMULATION MANIPULATIONS HERE
      if (info[7]== -1) 
	   return 1;
//-----------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------
//    PERFORM ANY 'AFTER-ITERATION' MANIPULATIONS THAT ARE REQUIRED HERE
//    e.g. save variables to storage array for the next timestep
      if (info[12]>0) 
      {
//	   nitems=0;
//	   stored[0]=... (if NITEMS > 0)
//        setStorageVars(STORED,NITEMS,INFO)
	     return 1;
      }
//-----------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------
//    DO ALL THE VERY FIRST CALL OF THE SIMULATION MANIPULATIONS HERE
      if (info[6]== -1) // first call of this component in the simulation
      {
//       SET SOME INFO ARRAY VARIABLES TO TELL THE TRNSYS ENGINE HOW THIS TYPE IS TO WORK
         info[5]=nout;				
         info[8]=1;				
	   info[9]=0;	// STORAGE FOR VERSION 16 HAS BEEN CHANGED				

//       SET THE REQUIRED NUMBER OF INPUTS, PARAMETERS AND DERIVATIVES THAT THE USER SHOULD SUPPLY IN THE INPUT FILE
//       IN SOME CASES, THE NUMBER OF VARIABLES MAY DEPEND ON THE VALUE OF PARAMETERS TO THIS MODEL....
         nin=1;
	       npar=5;
	       nder=0;
	       
//       CALL THE TYPE CHECK SUBROUTINE TO COMPARE WHAT THIS COMPONENT REQUIRES TO WHAT IS SUPPLIED IN 
//       THE TRNSYS INPUT FILE
    int dummy=1;
		TYPECK(&dummy,info,&nin,&npar,&nder);

//       SET THE NUMBER OF STORAGE SPOTS NEEDED FOR THIS COMPONENT
//         nitems=0;
//	     setStorageSize(nitems,info)

//       RETURN TO THE CALLING PROGRAM
         return 1;
      }


//-----------------------------------------------------------------------------------------------------------------------
//    DO ALL OF THE INITIAL TIMESTEP MANIPULATIONS HERE - THERE ARE NO ITERATIONS AT THE INTIAL TIME
      if (time < (getSimulationStartTime() +
       getSimulationTimeStep()/2.0)) 
       {
//       SET THE UNIT NUMBER FOR FUTURE CALLS
         iunit=info[0];
         itype=info[1];

//       CHECK THE PARAMETERS FOR PROBLEMS AND RETURN FROM THE SUBROUTINE IF AN ERROR IS FOUND
//         if(...) TYPECK(-4,INFO,0,"BAD PARAMETER #",0)

//       PERFORM ANY REQUIRED CALCULATIONS TO SET THE INITIAL VALUES OF THE OUTPUTS HERE
//		 output
			xout[0]=0;

//       PERFORM ANY REQUIRED CALCULATIONS TO SET THE INITIAL STORAGE VARIABLES HERE
//         nitems=0;
//	   stored[0]=...

//       PUT THE STORED ARRAY IN THE GLOBAL STORED ARRAY
//         setStorageVars(stored,nitems,info)

//       RETURN TO THE CALLING PROGRAM
         return 1;

      }
//-----------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------
//    *** ITS AN ITERATIVE CALL TO THIS COMPONENT ***
//-----------------------------------------------------------------------------------------------------------------------

	    
//-----------------------------------------------------------------------------------------------------------------------
//    RETRIEVE THE VALUES IN THE STORAGE ARRAY FOR THIS ITERATION
//      nitems=
//	    getStorageVars(stored,nitems,info)
//      stored[0]=
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//    CHECK THE INPUTS FOR PROBLEMS
//      if(...) TYPECK(-3,INFO,'BAD INPUT #',0,0)
//	if(IERROR.GT.0) RETURN 1
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//    *** PERFORM ALL THE CALCULATION HERE FOR THIS MODEL. ***
//-----------------------------------------------------------------------------------------------------------------------

//		ADD YOUR COMPONENT EQUATIONS HERE; BASICALLY THE EQUATIONS THAT WILL
//		CALCULATE THE OUTPUTS BASED ON THE PARAMETERS AND THE INPUTS.	REFER TO
//		CHAPTER 3 OF THE TRNSYS VOLUME 1 MANUAL FOR DETAILED INFORMATION ON
//		WRITING TRNSYS COMPONENTS.













//-----------------------------------------------------------------------------------------------------------------------

//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//    SET THE STORAGE ARRAY AT THE END OF THIS ITERATION IF NECESSARY
//      nitmes=
//      stored(1)=
//	    setStorageVars(STORED,NITEMS,INFO)
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//    REPORT ANY PROBLEMS THAT HAVE BEEN FOUND USING CALLS LIKE THIS:
//      MESSAGES(-1,'put your message here','MESSAGE',IUNIT,ITYPE)
//      MESSAGES(-1,'put your message here','WARNING',IUNIT,ITYPE)
//      MESSAGES(-1,'put your message here','SEVERE',IUNIT,ITYPE)
//      MESSAGES(-1,'put your message here','FATAL',IUNIT,ITYPE)
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//    SET THE OUTPUTS FROM THIS MODEL IN SEQUENTIAL ORDER AND GET OUT

//		 output
			xout[0]=42;

//-----------------------------------------------------------------------------------------------------------------------
//    EVERYTHING IS DONE - RETURN FROM THIS SUBROUTINE AND MOVE ON
      return 1;
      }
//-----------------------------------------------------------------------------------------------------------------------
// */