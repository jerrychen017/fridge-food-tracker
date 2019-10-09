# 2019-group-fridgefoodtracker

<h2>HOW TO START OUR APPLICATION</h2>

1) Start the server manually
- We haven't yet deployed a server yet, so you have to start it locally

  >   cd server
  >
  >   node index.js
  
- On Google Chrome, go to localhost:3000/fridge/
  - You should see the below page. By using the input fields in the top left, submitting, and refreshing the page, you should be able to see what items are currently in your fridge.
![Server_Screenshot](./docs/Pictures/Server_Screenshot_2019_10_09.PNG)


2) Open the app
- Open our project in Android Studio
- Run our MainActivity on an emulator
  - We are using a Pixel 3a API 29
- You should see the following screen:
![App_Screenshot](./docs/Pictures/App_MainActivity_Screenshot_2019_10_08.png)
- Enter a food item name in the input field at the top, and submit using the floating action button in the bottom right
  - The floating action button might be hidden behind the keyboard
- What you see on your emulator screen should match what you see in your web browser from step (1).


<h2>ITERATION 2</h2>

![App_Server_Screenshot](./docs/Pictures/App_Server_Screenshot_2019_10-08.png)

We've implemented manual data entry as our first feature.

We have unit tests written for Fridge, ItemList, and Item, and we have functional tests in Postman for our server.
