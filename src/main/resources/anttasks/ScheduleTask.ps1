# ScheduleTask.ps1

# Name of the task
$taskName = "MyNotepadTask"

# Path to the executable or script you want to run
$executablePath = "C:\Windows\System32\notepad.exe"

# Time to run the task (24-hour format)
$startTime = "10:00"

# Use schtasks to create the task
schtasks /create /tn $taskName /tr $executablePath /sc DAILY /st $startTime

Write-Output "Task scheduled!"
