<!-- These are placeholders that you must fill in. -->


# Project Proposal - Fridge Food Tracker

# Elevator Pitch

Find yourself throwing out tons of food when they go bad in your fridge? The Fridge food tracker will allow the user to input their various grocery items and the app will keep track of when the food will go bad. The app will notify you of any food that will go bad soon and should be used soon.

# Problem

Food waste

## Introduction to Domain

Lots of food is wasted in many households. There are many causes, and one prevalent cause is people simply forgetting what they have in their fridges and letting it all go bad. There is a need for some easy-to-use process for keeping track of what food is owned and when that food will go bad.

# Solution

People will input their grocery items into the app, which will then be tracked for how long you've kept each item. It will notify users of items that should be used soon, or items that should (reluctantly) be thrown out. According to the Food and Agriculture Organization of the United Nations, "roughly one third of the food produced in the world for human consumption every year - approximately 1.3 billion tons - gets lost of wasted. Specifically, in US households about 150,000 tons of food is tossed out in US households each day." This app should help this by keeping track of food that should be used.

## Architecture Overview

* Mobile App - Android Studio, Java SDK 12
* OCR API - receipt camera
* jcomo/StillTasty API - expiration dates
* Web Server - Node backend with RESTful API

## Features

* Interface to input grocery items
* GUI to see a virtual fridge
* Each item will have a graphic or some other fun way to display current shelf life and how much of item is left
* Shelf life determined by FDA - connect to FDA list (if it exists)
* Color gradient progess bar
* Notification system for when shelf life gets too short
* Take picture or receipt and add items automatically
* User profile/Social media sharing/statistics
* Novel idea - Recommendation - buy again based on comsumption, recipe integration, subtractions based on amount, nutrition recommendation
## Wireframes

**<!-- Description, for example, “Events Map” -->**

![WireframeCamera](Pictures/Wireframes\ Camera\ Input.JPG)

## User Stories

As a person using the application, I input my groceries into the app. The app should automatically add the shelf lives of the common items. The user will be prompted for a use by date if it is not a common item. Say I buy fresh ground beef. Fast forward 1 day, the app will notify me of the shelf life and amount of item that needs to be used soon, potentially offering recipes that use that item. If I don't use it and two more days go by, the app will notify me that the ground beef should be disposed.

# Viability

## Hardware

* Yes, we have all the necessary hardware.
* Phone

## APIs

* Integration with StillTasty API or shelf-life by jcomo
* API for reading receipt - OCR API


## Tools
* Android Studio: Because we have experience with it
* Java
* Javascript/React

## Proof of Concept

One can look at Tab, which is a mobile  app that takes a photo of a receipt and splits the tab, for the validity of virtually entering items into a virtual fridge. The GUI interface can be used similar to any sort of tracking app. 

# Difficulty

This project is sufficiently interesting because we have to track inputs, show notifications, and we have to solve the problem of integrating API's for keeping track FDA regulations. We also need to implement a receipt recognizer, alongside some luxury features like recipe suggester.

# Market Research

## Users

Basically anyone who does groceries. Restaraunts and grocery stores could also use the app, potentially a web version, the keep track of items in stock and what items to replace.

## Competition

FreshFridge app. Ours would be different by offering a more GUI focused application alongside the ability to take a photo of the receipt and automatically upload it to the fridge.

# Roadmap

https://github.com/jhu-oose/2019-group-fridgefoodtracker/projects/1
