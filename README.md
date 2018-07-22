# Java JNI wrapper for GNU OCRAD
This project uses GNU OCRAD to perform OCR using java.

## Getting Started
Supports PNM(PBM,PPM,..etc) formats for now.

### Prerequisites
Java 1.8 32 bit (64 bit doesn't work)

### Runnig project
* Import this project into Eclipse.
* Add dll path to Native library location.
  ![image](https://user-images.githubusercontent.com/8513424/43042833-0b2ead16-8da4-11e8-97aa-f0d5040b2c7e.png)
* Run Ocrad.java , the test image file is located in `resources/test.pbm`.
* It should print

```
0.26
00123456789
ABCDEFGHIJKLMN
ÑOPQRSTUVWXYZ
abcdefghijklmn
ñopqrstuvwxyz
ÁÉÍÓÚ ÀÈÌÒÙ ÄËÏÖÜ
ÂÊÎÔÛ ??´ å¨ý
áéíóú àèìòù äëïöü âêîôû
!"#$%&'()*+,-./:;<=>?
@[\]^_`{|}~¡ª¬°
```

## Development
If you just want to compile the library for linux skip this step and go to Compiling step.
### Generate header file
* Add external tools configuration to generate header file.
  ![image](https://user-images.githubusercontent.com/8513424/43042947-3515a9ac-8da6-11e8-8866-b3a20440e164.png)
  
* Enter the configuration as provided below the image.
  ![image](https://user-images.githubusercontent.com/8513424/43043076-91be24b6-8da8-11e8-9410-53cd05dac1d9.png)
* Program -> Right click -> New Configuration
* Location: `C:\Program Files\Java\jdk1.8.0_102\bin\javah.exe`
* Working Directory: `${workspace_loc:/${project_name}/src}`
* Arguments: 

`-classpath ${project_classpath} -v -d ${workspace_loc:/${project_name}/src/main/resources} ${java_type_name}`
* Select Main.java file and then click run external tools JNI.
* The header file will be generated in `JOCRAD\src\main\resources\` as `com_ocrad_Main.h`

### Implementation of com_ocrad_Main.h
The basic functions of `ocradlib` were implemented in `com_ocrad_Main.cc` file.

## Compiling 
Precompiled dll already available for Windows in `src/main/resources/dll` folder, if you want to compile for linux please get the following repository 
```
https://github.com/t-pankajkumar/OCRAD
```
### Steps to compile
* Compile jni implimentation
  `g++ -c -fPIC -I"C:/Program Files/Java/jdk1.8.0_102/include" -I"C:/Program Files/Java/jdk1.8.0_102/include/win32" com_ocrad_Main.cc -o com_ocrad_Main.o`
* Generate dll
  `g++ -shared -o native.dll com_ocrad_Main.o common.o segment.o mask.o rational.o rectangle.o track.o iso_8859.o ucs.o user_filter.o page_image.o page_image_io.o bitmap.o blob.o profile.o feats.o feats_test0.o feats_test1.o character.o character_r11.o character_r12.o character_r13.o textline.o textline_r2.o textblock.o textpage.o -Wl,--add-stdcall-alias`
* Move the generated `native.dll` to `src/main/resources/dll`.
* Run `Ocrad.java`.
* BINGO
