#cs ----------------------------------------------------------------------------

 AutoIt Version: 3.3.14.2
 Author:         myName

 Script Function:
	Template AutoIt script.

#ce ----------------------------------------------------------------------------

; Script Start - Add your code below here
ControlFocus("Open","","Edit1")
Sleep(2000)
ControlSetText("Open","","Edit1","D:\Ravneet\RFN 2.0 Master\RFN2.0_Ravneet\SETUPS\SecondaryUsersList.csv")
ControlClick("Open","","Button1")
Sleep(2000)