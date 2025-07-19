<div style="display: flex;flex-direction: row;justify-content: center" width="100%">
      <img src="./img/logo.png"></img>
</div>

## AgentWeb Introduction

AgentWeb is an Android WebView-based library that is extremely easy to use and powerful, providing a series of solutions for Android WebView problems, and is lightweight and extremely flexible. For detailed usage, please refer to the Sample above.
	

## Gradle Import

```groovy
allprojects {
  repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
  }
}
```



* Androidx

   ```groovy
    implementation 'io.github.justson:agentweb-core:v5.1.1-androidx' 
    implementation 'io.github.justson:agentweb-filechooser:v5.1.1-androidx' // (optional)
    implementation 'com.github.Justson:Downloader:v5.0.4-androidx' // (optional)
   
   ```


## Related Projects
* [flying-pigeon Cross-process IPC component](https://github.com/Justson/flying-pigeon)
* [AgentWebX5](https://github.com/Justson/AgentWebX5)
* [WebView Progress Bar](https://github.com/Justson/CoolIndicator)
* [Downloader A lightweight file downloader](https://github.com/Justson/Downloader)

	


## Important Notes
* Alipay usage requires importing the Alipay SDK and depending on it in the project. WeChat Pay requires no additional operations.
* AgentWeb internally uses `AlertDialog` which requires dependency on `AppCompat` theme.
* `setAgentWebParent` does not support `ConstraintLayout`.
* `mAgentWeb.getWebLifeCycle().onPause();` will pause all `WebView` in the application.
* For `minSdkVersion` lower than or equal to 16, please pay attention to communication security between custom `WebView` and `JS`.




## Documentation Help
* [Wiki](https://github.com/Justson/AgentWeb/wiki) (incomplete)
* `Sample` (recommended, detailed) 
* [Release Notes](./releasenote.md)



## Issues or Better Suggestions
* [![QQ0Group][qq0groupsvg]][qq0group]
* Welcome to submit [Issues](https://github.com/Justson/AgentWeb/issues)


## Donation
Open source is not easy, your support is my motivation to update.

<a href="img/alipay.jpg"><img src="img/alipay.jpg" width="30%"/></a> <a href="img/wechat_pay.jpg"><img src="img/wechat_pay.jpg" width="30%"/></a> <a href="img/alipay.jpg"><img src="img/alipay.jpg" width="30%"/></a>


[licensesvg]: https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg
[license]: https://github.com/Justson/AgentWeb/blob/master/LICENSE

[qq0groupsvg]: https://img.shields.io/badge/QQ群-599471474-fba7f9.svg
[qq0group]: http://qm.qq.com/cgi-bin/qm/qr?k=KpyfInzI2nr-Lh4StG0oh68GpbcD0vMG

 

[![License][licensesvg]][license]

## License 
```
Copyright (C)  Justson(https://github.com/Justson/AgentWeb)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

​	

​