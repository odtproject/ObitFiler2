; ObituaryFiler installer using NSIS Modern User Interface
; by Alice Ramsay

; some definitions to make it easier to set up the uninstall settings
; in Add/Remove Programs
!define APPNAME "${APP_NAME}"
!define DISPNAME "${DISPLAYNAME}"
!define PROGRAMFILE "${EXE_NAME}"
!define ICON dove_purple.ico
!define ICONFULLPATH "${FOLDER}\images\${ICON}"
;!define GIFFILE "${FOLDER}\images\dove_purple.gif"
!define UNINSTNAME "un${APPNAME}.exe"
!define PUBLISHER "Obituary Daily Times"

; These define the version number and must be integers
!define VERSIONMAJOR ${VERSIONMAJ}
!define VERSIONMINOR ${VERSIONMIN}
!define VERSIONBUILD ${VERSIONBLD}
!define VERSIONSTRING "${APPNAME}${VERSIONMAJOR}${VERSIONMINOR}"
!define VERSIONDOTTED "${VERSIONMAJOR}.${VERSIONMINOR}.${VERSIONBUILD}"

; This is the size (in kB) of all the files copied into "Program Files"
!define INSTALLSIZE 136

;--------------------------------
;Include files

  !include MUI2.nsh ; for Modern UI
  !include LogicLib.nsh ; for logical macros
  !include FileFunc.nsh ; for file functions

;--------------------------------
;General

  ;Name and file
  ; This will be in the installer/uninstaller's title bar
  Name "${DISPNAME}"

  ; define the icon and name of the installer file
  Icon "${ICONFULLPATH}"
  Outfile "${VERSIONSTRING}ins.exe"
  
  ; define the directory to install to
  ; there's bug in the Program Files decoding that messes up the icon setting
  InstallDir "$PROGRAMFILES\${APPNAME}"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\${APPNAME}" ""

  ; require administrator rights
  RequestExecutionLevel admin
  VIProductVersion "${VERSIONDOTTED}"

;--------------------------------
;Variables

  Var StartMenuFolder
  
;--------------------------------
;Interface Settings

  !define MUI_ICON ${ICONFULLPATH}
  !define MUI_UNICON ${ICONFULLPATH}
  !define MUI_ABORTWARNING

;--------------------------------
;Pages

	!define MUI_FINISHPAGE_NOAUTOCLOSE ; don't close the installer at the finish
	
	!insertmacro MUI_PAGE_WELCOME
	!insertmacro MUI_PAGE_DIRECTORY
  
	;Start Menu Folder Page Configuration
	!define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKCU" 
	!define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${APPNAME}" 
	!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
  
	!insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder

	; installation page
	!insertmacro MUI_PAGE_INSTFILES
  
	; uninstall pages
	!insertmacro MUI_UNPAGE_CONFIRM
	!insertmacro MUI_UNPAGE_INSTFILES
  
	; nice finish page
	!insertmacro MUI_PAGE_FINISH

  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Macros

	; use this macro to verify that the user has
	; administrator rights
	!macro VerifyUserIsAdmin
	UserInfo::GetAccountType
	pop $0
	${If} $0 != "admin" ;Require admin rights
        messageBox mb_iconstop "Administrator rights required!"
        setErrorLevel 740 ;ERROR_ELEVATION_REQUIRED
        quit
	${EndIf}
	!macroend  

;--------------------------------
;Functions

  ; call the macro on init of the installer
  function .onInit
		setShellVarContext all
		!insertmacro VerifyUserIsAdmin	
  functionEnd

  ;--------------------------------
  ;Installer Sections

Section "" ; default section

	SetOutPath "$INSTDIR"		
  
	; define what to install
	File "${FOLDER}\build\${PROGRAMFILE}"
	File "${ICONFULLPATH}"
;	File "${GIFFILE}"
	
	; registry keys that Windows needs
	WriteRegStr HKLM "SOFTWARE\${APPNAME}" "" "$INSTDIR"

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayName" "${DISPNAME}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "UninstallString" "$\"$INSTDIR\${UNINSTNAME}$\""

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "InstallLocation" "$\"$INSTDIR$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayIcon" "$\"$INSTDIR\${ICON}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "Publisher" "${PUBLISHER}"

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayVersion" "${VERSIONDOTTED}"
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "VersionMajor" ${VERSIONMAJOR}
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "VersionMinor" ${VERSIONMINOR}

	; There is no option for modifying or repairing the install
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "NoRepair" 1

	; Set the INSTALLSIZE constant (!defined at the top of this script) so Add/Remove Programs can accurately report the size
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "EstimatedSize" ${INSTALLSIZE}

	; write out the uninstaller
	WriteUninstaller "$INSTDIR\${UNINSTNAME}"
	
	!insertmacro MUI_STARTMENU_WRITE_BEGIN Application  
    
		;Create shortcuts
		CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
		CreateShortCut "$SMPROGRAMS\$StartMenuFolder\${APPNAME}.lnk" "$INSTDIR\${PROGRAMFILE}" "" "$INSTDIR\${ICON}"
		CreateShortcut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\${UNINSTNAME}"
  
	!insertmacro MUI_STARTMENU_WRITE_END

	; create desktop shortcut
	SetOutPath "$DESKTOP"
	CreateShortCut "$DESKTOP\${APPNAME}.lnk" "$INSTDIR\${PROGRAMFILE}" "" "$INSTDIR\${ICON}"


  SectionEnd ; end of default section
  
  ;--------------------------------
  ;Install the other user files
  
Section "UserSetup"

	; create the ODT folder 
	var /GLOBAL ODTDIR
	StrCpy $ODTDIR "$PROFILE\ODT"
	CreateDirectory "$ODTDIR"

	; create the lib folder and put the abbreviations file in it
	var /GLOBAL LIBDIR
	StrCpy $LIBDIR "$ODTDIR\lib"
	SetOutPath $LIBDIR

	File "${FOLDER}\info\abbrev.txt"

	; create the appdata folder
	CreateDirectory "$ODTDIR\appdata"
	
SectionEnd # end UserSetup

  ;--------------------------------
  ;Uninstall section

Section Uninstall

	; have to set this for the uninstall or the shortcuts don't get removed
	setShellVarContext all

	; delete desktop shortcut
	Delete "$DESKTOP\${APPNAME}.lnk"

	; delete the start menu shortcuts and folder
	!insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
	Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
	Delete "$SMPROGRAMS\$StartMenuFolder\${APPNAME}.lnk"
	RMDir "$SMPROGRAMS\$StartMenuFolder"

	; delete the program itself and the logo icon
	Delete "$INSTDIR\${PROGRAMFILE}"
	Delete "$INSTDIR\${ICON}"
;	Delete "$INSTDIR\${GIFFILE}"	

	; delete registry keys
	DeleteRegKey HKLM "SOFTWARE\${APPNAME}"
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}"

	; Delete the uninstaller itself
	Delete "$INSTDIR\${UNINSTNAME}"

	; Deelete the installation folder -- this will only work if it's empty
	RMDir  "$INSTDIR"

SectionEnd # end of uninstall section