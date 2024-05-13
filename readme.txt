
Smart-TimeTrack-Astrelya : application designed to record employees' arrival and departure times in an Excel file.

1 How to install :

Install in dev mode:
  - Clone the repository from the following link: https://github.com/ahmedbakh/NFCTimeTrack.
  - Connect an NFC Reader to a USB port on your computer
  - launch the application in an Integrated Development Environment (IDE) that supports Java 20 or higher.
Build as fat jar:
  - Run the  maven command clean package
Install as Client
  - Connect an NFC Reader to a USB port on your computer.
  - Double click on start.bat

Once the NFC reader is connected and the Smart-TimeTrack-Astrelya app is running, it will wait for NFC cards to be scanned. After a card is scanned, the app searches for the UID of the card in the file located at "D:\AstrelyaTimeTrackApp\EmplyeeCardAssign\employes_cards_config.txt."
There are two possible scenarios:

    - Card Not Assigned to an Employee:
        If the card is not linked to any employee, the app will append a new line in the "employes_cards_config.txt" file with the card ID followed by "Carte non attribuée."
        It is the responsibility of the HR department to update "Carte non attribuée" with the correct employee's name.

    - Card Already Assigned to an Employee:
        If the card is already assigned to an employee, the app will locate the ID in the "employes_cards_config.txt,"
        retrieve the associated employee name, and record this event in the "TimeTrack_2024.xlsx" file under "D:\AstrelyaTimeTrackApp\TimeTrackExcel".





