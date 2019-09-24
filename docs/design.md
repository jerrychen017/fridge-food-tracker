# Design

# Architecture

<!-- Is this a web application, 
a mobile application (React Native, iOS, Android?), 
a desktop application, and so forth? 
How do the different components (client, server, and so forth) communicate? 
Don’t simply list tools; tell a story. -->
* Mobile App - Android Studio, Java SDK 12 

Our application is an Android application with Java 12.  

* OCR API - receipt camera

It would be convenient for users to have multiples ways to input items to the app. Therefore, we
're using OCR to extract items from receipts. More specifically, we included Google Firebase
 Vision ML SDK to our application so that we can use text recognition APIs (https://firebase.google.com/docs/ml-kit/android/recognize-text). 
  
* jcomo/StillTasty API - expiration dates
 
 To obtain common food expiration dates, we use 
 StillTasty API developed by jcomo(https://github.com/jcomo/shelf-life/tree/master/src/main/java/me/jcomo/stilltasty).

* Web Server - Node backend with RESTful API

For the server side, we use Node.js and RESTful API

* Frontend 
We make XML through Layout Editor in Android Studio because it's built-in and simple. 

## Tools Outside the Toolbox

<!-- For each tool: What is it? Why did you choose it? 
Where do you get it?
 How do you learn it? 
 Follow the model of how we presented the tools in the Toolbox. 
 Cute original drawings encouraged. -->
 We mentioned these tools above in the Architecture section. Here's a summary of them: 
 
 #### Android Studio 
 * What is it? 
 Android Studio is the official integrated development environment for Google's Android operating
  system.
 * Why Did We Choose It?
 Because we  
 * Where Do I Get It? 
 * How Do I Learn It? 
 
#### Android Studio 
  * What is it? 
  * Why Did We Choose It? 
  * Where Do I Get It? 
  * How Do I Learn It? 
  
#### Android Studio 
   * What is it? 
   * Why Did We Choose It? 
   * Where Do I Get It? 
   * How Do I Learn It? 
   
 #### Android Studio 
 * What is it? 
 * Why Did We Choose It? 
 * Where Do I Get It? 
 * How Do I Learn It?    
   
We attached the tool links to the items and that's where we can look up for documentations. We
 can learn them from the official tutorials published on these websites. 

# Class Diagram

![Class Diagram](./classDiagrams/diagram_screenshot.png)
Design patterns that we used are explained in the class diagram. 
