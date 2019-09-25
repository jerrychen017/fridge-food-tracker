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
 Vision ML SDK to our application so that we can use [text recognition APIs] (https://firebase.google.com/docs/ml-kit/android/recognize-text). 
  
* jcomo/StillTasty API - expiration dates
 
 To obtain common food expiration dates, we use 
 [StillTasty API] (https://github.com/jcomo/shelf-life/tree/master/src/main/java/me/jcomo/stilltasty) developed by jcomo.

* openFDA - give notifications about food recalls and overall food safety alerts
One feature to add further down the line is to be able to alert users of food that the FDA has put alerts out on so that users will be notified about what food to avoid in their current fridge and what food to avoid shopping for in their next grocery trip. Here is the [API](https://open.fda.gov/)

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
 Android studio is the official IDE for android app development and we'd like to use the standard.
 * Where do you get it
 Android studio is available [here] (https://developer.android.com/studio).
 * Where do you learn it?
 It can be learned through multiple tutorials online or by just reading through the official API documentation.
#### ML Kit
  * What is it? 
  ML Kit is Google's machine learning library optimized for mobile development.
  * Why Did We Choose It? 
  We chose this library due to the open source library for both text recognition and barcode recognition which we will use in order to input items into our fridge.
  * Where Do I Get It? 
  The tutorial for integrating ML Kit can be found [here] (https://developers.google.com/ml-kit/).
  * How Do I Learn It? 
  The open source documentation is availble online as well as simple tutorials provided by Google. 

#### jcomo/StillTasty API
   * What is it? 
   * Why Did We Choose It? 
   * Where Do I Get It? 
   * How Do I Learn It? 

#### RESTful API
   * What is it? 
   * Why Did We Choose It? 
   * Where Do I Get It? 
   * How Do I Learn It? 

#### Node.js 
 * What is it? 
 * Why Did We Choose It? 
 * Where Do I Get It? 
 * How Do I Learn It?    
   
We attached the tool links to the items and that's where we can look up for documentations. We
 can learn them from the official tutorials published on these websites. 

# Class Diagram

![Class Diagram](./classDiagrams/diagram_screenshot.png)
Design patterns that we used are explained in the class diagram. 
