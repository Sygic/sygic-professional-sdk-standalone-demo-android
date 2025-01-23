# sygic-professional-sdk-standalone-demo-android


Demo app for [Standalone integration using Android Studio](https://www.sygic.com/developers/professional-navigation-sdk/android/getting-started/standalone-integration-using-android-studio).

The code provided in this repository is intended solely for the purpose of demonstrating how an API works and should not be considered production-ready or suitable for use in real-world applications.

1. All API calls in our lib must be done and handled in not UI Thread.
   You are free to choose any of approach to do that:
   AsyncTask, Kotlin coroutines, Threads, WorkManager and so on.
   All responses will be received in the same thread where
   you call it.
2. All API calls internally implemented as FIFO query and will be handled one by one.
3. Timeout in API call will be handled only when API function going to work.
 
