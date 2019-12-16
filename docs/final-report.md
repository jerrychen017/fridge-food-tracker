# Final Report

**⚠️  Remember to also fill in the individual final reports for each group member at `https://github.com/jhu-oose/2019-student-<identifier>/blob/master/final-report.md`.**

**⚠️  You don’t need to do anything special to submit this final report—it’ll be collected along with Iteration 6.**

# Revisiting the Project Proposal & Design

Link to our original project proposal: https://github.com/jhu-oose/2019-group-fridgefoodtracker/blob/master/docs/project-proposal.md

We set out to build an app that reduces food waste by keeping users aware of what they have in their fridge and when that food will go bad. We wanted to make the app easy to use, with features like being able to add to your virtual fridge in multiple ways, automatically suggesting common expiration durations, and recommending items that users have purchases before. Our full feature list is copied below: 

* Interface to input grocery items
* GUI to see a virtual fridge
* Each item will have a graphic or some other fun way to display current shelf life and how much of item is left
* Shelf life determined by FDA - connect to FDA list (if it exists)
* Color gradient progess bar
* Notification system for when shelf life gets too short
* Take picture or receipt and add items automatically
* User profile/Social media sharing/statistics
* Novel idea - Recommendation - buy again based on comsumption, recipe integration, subtractions based on amount, nutrition recommendation

We looked back on our project proposal many times through the semester. It helped us keep focus on the core features of our app, and the wireframes acted as guides when we weren't sure what our user interface should be.

We mostly stuck with our original proposal and wireframes. While we weren't able to complete our full feature list, we think our app is a functional baseline that we can continue to improve after OOSE ends. 

<!--
How did the Project Proposal & Design documents help you develop your project?

What changed in your project since you wrote the initial version of those documents?
-->

# Challenges & Victories

Software Engineering is hard. We struggled with a few different bugs throughout the semester.

### Asynchronization

Our client backs up fridge data to a Heroku server. This allowed us to easily share fridges between multiple devices. It also gave us many struggles. 

Several parts of our app need to wait for the Response payload before continuing in order to work properly. We struggled with using async tasks to get our fridge to load properly before we displayed it and to grab the correct item ID before we deleted the wrong thing.

### Third Party APIs

One of the APIs that we relied on, StillTasty, changed halfway through the semester. After we adapted our code to work with the changes, we found that we still we're getting the results we wanted. We chose to pivot and build our own small database of common expiration durations. We plan to release the database publicly in case future teams want to use it.

<!--
In software engineering things rarely go as planned: tools don’t work as we expect, deadlines aren’t met, debugging sessions run longer than we hoped for, and so forth.

What were some of the biggest challenges you found when developing your project? How did you overcome them?
-->

# Experience with Tools

### Android

Android is ubiquitous. It's been around for a long time, and there are tons of devices throughout the world running it. We wanted our app to run on as many of these devices as possible, so we set our minimum APK version to be quite low. This meant we couldn't take advantage of many of the quality-of-life updates Android has had over the years, and so it sometimes felt like we were writing legacy software right out of the gate.

### Git and GitHub

This is many of our first times working in a larger team. As such, it's the first time many of us have adopted a branching & merging workflow. We did have some troubles, especially at first, but we were all able to implement this flow quickly. We had good success with this workflow, and we would be happy to do it again.

### Postman

We used Postman to test our Server calls. It was pretty good, and no serious problems occurred.

### Heroku

We used Heroku to host our Server. It was pretty good, and no serious problems occurred. The only problem we ran into was that Heroku only natively supports PostgreSQL, so we had to majorly refactor our server which had been relying on an SQLite database up until deployment -- as apparently PostgreSQL has quite a few major divergences from other SQL languages.

### JUnit

We used JUnit to test many of the classes that we had created. It helped us find some of the bugs we were having more quickly. The tool would have been more useful if we had integrated with Travis CI so that the tests ran automatically, but with our small codebase we found that running the tests manually was fine. YAGNI.

### Google ML Kit

Google ML Kit was absolutely critical to the implementation of Barcode and OCR processing. Equally critical were their tutorial and sample apps - which we relied heavily on. Our only complaint would be the number of helper-classes required to implement even the simplest capabilities of ML Kit. Ultimately though, having explored other software options for implementing Barcode and OCR processing, we have no regrets about choosing ML Kit.

### Node JS

Node JS was the foundation of our backend. Our server was a Node Express server, and it allowed us to create an easily scalable and quick to respond server. Node was a natural choice for our backend with the huge amount of support for data management and asynchronous functionality, and we are glad we went with Node rather than Javalin for our backend, as it allowed us to more quickly iterate and add features when necessary.

<!--
Which tools did you learn to like? Why?

Which tools did you learn to dislike? Why? And what other tools would you have replaced them with if you were to start all over again?
-->

# Iteration 7 & Beyond

We would finish some of the features in our project proposal that we couldn't get to in time, like recommending items based on known recipes. Beyond these, we would want to release the app on iOS and desktop, 

<!--
Where would you take your project from here? What features would you add to make your application even more awesome? How would you prioritize that work?

Update the project board with tasks for a hypothetical Iteration 7.
-->

# A Final Message to Your Advisor

Thank you for your guidance throughout the project. Many of the suggestions that you have made we were able to incorporate and implement into our project.
<!--
What did you like in working with them?

What do you think they need to improve?

And anything else you’d like to say.
-->
