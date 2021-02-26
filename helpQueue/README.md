***Admin***

    This class was the code that run the fucntion received from AdminGUI, Inside this have
    the function connect with server.

***AdminGUI***

    This was the Gui for Admin to create class and course, setup the time and date for 
    each course and can delete any student or course.

***Client***

    This class was the code that run the function received from ClientGui, Inside this have
    the function with server.
    
***ClientGUI***
    
    This class was the code for Client to ask request and cancel for help. 
    
***Server***
    
    This class was connect with everythig,like Admin ,client and display etc...
    

***DataAccessObject***

    This is the object class that provides access to an underlying database.    
    
***Event***     

    This class provides constructor to set initial values for event and includes getters and setters to access and update the value of each attributes.  

***HelpQueue***
   
   This class provides constructor to set initial values for clients and gets/sets client's workStationName, requestTime, cancelTime, status, and their waitTime
   
***HelpQueueDAO***
    
    This class provides all the methods that helps clientHandler to make changes and gets informations from the database.
    
***Display***
    
    This is the conntrol class which can only talk to the server. It recieved info from the server and calls DisplayGui
    to display clients' workStaionName, requestTime, and waitTime. It'll also display currentTime, courseName, and sectionName.
    It will be refresh every 3 seconds.
    
***DisplayGui***

    This is the class that provide the layout for display by extends JFrame. 
    
***ClientHandler***

    This class handler changes for clients, admin once the clients or admin is connected to the server. 
    