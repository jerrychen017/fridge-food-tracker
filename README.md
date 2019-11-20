# 2019-group-fridgefoodtracker

<h2>HOW TO START OUR APPLICATION</h2>

1) Use our deployed server at https://oose-fridgetracker.herokuapp.com/fridge

Or if you don't want to use our server, start it yourself.

To start the server manually go to /src/server and type the following commands:
```
npm init
npm start
```

- On Google Chrome, go to localhost:3000/fridge/ if you are running the server manually or https://oose-fridgetracker.herokuapp.com/fridge
  - You should see the below page. By using the input fields in the top left, submitting, and refreshing the page, you should be able to see what items are currently in your fridge. TODO
![Server_Screenshot](./docs/Pictures/Server_Screenshot_2019_10_09.PNG)


2) Open the app
- Open our project in Android Studio
- Run our MainActivity on an emulator
  - We are using a Pixel 3a API 29


3) Using the app TODO
![App_Screenshot](./docs/Pictures/App_MainActivity_Screenshot_2019_10_08.png)
- Hit the floating action button in the bottom right to open the Items Entry fragment
  - The floating action button might be hidden behind the keyboard
- What you see on your emulator should match what you see on our Heroku server.
  - You will need to refresh your web browser to see the updated database in browser.


Troubleshooting
- Server doesn't start
  - Install npm with 
  ~~~
  npm install
  ~~~
  
- I don't have an emulator
  - Go to emulator -> Open AVD Manager -> Create Virtual Device
  - Select Pixel 3a -> Next
  - Select 'Q' as system image (API Level 29) -> Next -> Finish
  
- My configuration isn't working
  - Create an Android app with Module 'app'


<h2>ITERATION 2</h2>

![App_Server_Screenshot](./docs/Pictures/App_Server_Screenshot_2019_10-08.png)

We've implemented manual data entry as our first feature.

We have unit tests written for Fridge, ItemList, and Item, and we have functional tests in Postman for our server.

We have begun implementation of barcode-reading, and our progress can be seen in BarcodeScanner project. We will integrate the scanner in iteration 3. 

<h2>ITERATION 3</h2>

We've begun the modification of tableview for Fridge, showing the item as well as a button for deletion. We plan to have the button to get more info about the food item, as well as add some sort of progress bar showing the freshness of the item.

We've merged OCR functionality with the main app.

We've remodeled our item class to work better with our server.

![App_Screenshot_Iteration3](./docs/Pictures/app_screenshot_10.22.2019.PNG)

Due to how Heroku works, the majority of the server work has been splintered off to the server_deployed branch as Heroku needs the deployed application to be at the highest directory level of the project. You will find the latest server code and commits on the server on that branch.


