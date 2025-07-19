* v_5.0.0 Updates
	* ActionActivity refactored, using Fragment to replace Activity, solving multi-process usage issues
	* Added WebRTC Sample
	* Added FileCompressor, allowing file operations after file selection, such as file compression, image orientation adjustment, etc.
	* DefaultWebClient#onReceivedSslError added default handling
	* File chooser supports multi-selection
	* Fixed #777, FileChooserParams.createIntent() causing AcceptTypes loss issue
	* androidx Grade version upgrade to 7.0.2
	* Added AgentWebCompat.setDataDirectorySuffix(context) to fix "Using WebView from more than one process" crash

* v_4.1.1 Updates
    * [#587](https://github.com/Justson/AgentWeb/pull/587) input supports video recording
    * [#614](https://github.com/Justson/AgentWeb/pull/614) Fixed compatibility bug in file upload selection
    * Refactored Download
    * Minimum SDK raised to 14
    
* v_4.0.3 Updates
	* Some phones keep making sound during download process [#523](https://github.com/Justson/AgentWeb/issues/523)
	* Extracted [Downloader](https://github.com/Justson/Downloader)
	* Abandoned reflection callback WebViewClient#methods, using onion model Middleware instead

* v_4.0.2 Updates
	* Fixed progress calculation error during resume download
	* Fixed inability to close progress notification through `Extra`

* v_4.0.0 Updates
	* `AgentWeb` split into `AgentWeb-Download`, `AgentWeb-FileChooser`, `AgentWeb-core` three libraries, users can choose as needed
	* Redesigned `AgentWeb-Download`
	* Removed `DownloadListener`, `DefaultMsgConfig` and related APIs
	* Old deprecated APIs are directly removed in 4.0.0, no longer providing compatibility
	* Some classes and APIs renamed
	* `Fragment` and `Activity` construction consistency. [#227](https://github.com/Justson/AgentWeb/issues/227)
	* Removed `BaseAgentWebFragment` and `BaseAgentWebActivity` from AgentWeb-core, provided as Sample reference
* v_3.1.0 Updates
	* `WebProgress` progress bar animation more refined
	* Fixed some phone models where photo file size is 0
	* Updated `FileUpLoadChooserImpl`
* v_3.0.0 Updates
	* Added `MiddlewareWebChromeBase` middleware, supporting multiple `WebChromeClient`
	* Added `MiddlewareWebClientBase` middleware, supporting multiple `WebViewClient`
	* Added default error page and support for custom error pages
	* Added `AgentWebUIController` for unified UI control
	* Support for intercepting unknown pages
	* Support for calling other applications
* v_2.0.1 Updates
	* Support for parallel downloads, fixed #114 #109
* v_2.0.0 Updates
	* Added dynamic permissions
	* Camera support
* v_1.2.6 Updates
	* Fixed layout disorder on Android 4.4 and below
* v_1.2.5 Updates
	* Prompt messages support configuration
* v_1.2.4 Updates
	* Support for passing IWebLayout, supporting pull-to-refresh and bounce effects
* v_1.2.3 Updates
	* Added download result callback
* v_1.2.2 Updates
	* Fixed known bugs
* v_1.2.1 Updates
	* Support for calling Alipay and WeChat Pay
* v_1.2.0 Updates
	* Full support for fullscreen video
* v_1.1.2 Updates
	* Improved functionality