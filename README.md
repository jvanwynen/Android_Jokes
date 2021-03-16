# About Joke application

In this project I build an Android application using MVVM architecture. I used some Jetpack libraries, Kotlin Coroutines and Dagger Hilt. The app uses the [Joke API](https://jokeapi.dev/) as a remote data source.

# Screenshots
<p align="center">
  <img src="" width="200">
  <img src="" width="200">
</p>

# The Project Architecture
### Communication between layers
1. UI calls method from ViewModel.
2. Logic is handled in Viewmode;s
3. Viewmodels send LiveData or Events back to UI
4. The viewModels ask the respository for data which will come in form of Flows or with coroutines. 
5. The repository communicates with the Local storage with Flows. 

I made a diagram to show the flow of the data between the three layers.
![System Architecture](screenshots/dataFlowDiagram.png )


Libraries Used
---------------
* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
* [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow) - enable flows to optimally emit state updates and emit values to multiple consumers..
* [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - bind UI components to data sources
* [Material](https://material.io/develop/android/docs/getting-started/) - Material Components.
* [Coroutine](https://github.com/Kotlin/kotlinx.coroutines#user-content-android) - performs background tasks
* [Flows](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - for asynchronous data streams
* [Retrofit2](https://square.github.io/retrofit/)- networking
* [Gson](https://github.com/google/gson) - JSON Parser
* [Dagger2](https://dagger.dev/users-guide) - dependency injector
* [Stetho](http://facebook.github.io/stetho/) - debug bridge
* [Espresso](https://developer.android.com/training/testing/espresso/) // UI test
* [Barsita](https://github.com/AdevintaSpain/Barista) -UI tests Built on top of Espresso
* [Junit](https://junit.org/junit4/) // unit tests
* [Truth](https://github.com/google/truth) // Makes your test assertions and failure messages more readable


# To be added
* Testing

<br />
