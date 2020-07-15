# ImageControllerApi

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteImageUsingDELETE**](ImageControllerApi.md#deleteImageUsingDELETE) | **DELETE** /api/image/{id} | 删除image
[**getAllImagesUsingGET**](ImageControllerApi.md#getAllImagesUsingGET) | **GET** /api/image/all | 获取所有image列表
[**getImageByAppNameUsingGET**](ImageControllerApi.md#getImageByAppNameUsingGET) | **GET** /api/image/app | 根据appName获取image列表
[**getImageUsingGET**](ImageControllerApi.md#getImageUsingGET) | **GET** /api/image/{id} | 根据id获取image
[**getImagesByParamUsingGET**](ImageControllerApi.md#getImagesByParamUsingGET) | **GET** /api/images | 根据获取image列表


<a name="deleteImageUsingDELETE"></a>
# **deleteImageUsingDELETE**
> String deleteImageUsingDELETE(id)

删除image

### Example
```java
// Import classes:
//import ApiException;
//import ImageControllerApi;


ImageControllerApi apiInstance = new ImageControllerApi();
Long id = 789L; // Long | id
try {
    String result = apiInstance.deleteImageUsingDELETE(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageControllerApi#deleteImageUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getAllImagesUsingGET"></a>
# **getAllImagesUsingGET**
> List&lt;ImageEntity&gt; getAllImagesUsingGET()

获取所有image列表

### Example
```java
// Import classes:
//import ApiException;
//import ImageControllerApi;


ImageControllerApi apiInstance = new ImageControllerApi();
try {
    List<ImageEntity> result = apiInstance.getAllImagesUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageControllerApi#getAllImagesUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;ImageEntity&gt;**](ImageEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getImageByAppNameUsingGET"></a>
# **getImageByAppNameUsingGET**
> List&lt;ImageEntity&gt; getImageByAppNameUsingGET(appName)

根据appName获取image列表

### Example
```java
// Import classes:
//import ApiException;
//import ImageControllerApi;


ImageControllerApi apiInstance = new ImageControllerApi();
String appName = "appName_example"; // String | appName
try {
    List<ImageEntity> result = apiInstance.getImageByAppNameUsingGET(appName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageControllerApi#getImageByAppNameUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **appName** | **String**| appName |

### Return type

[**List&lt;ImageEntity&gt;**](ImageEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getImageUsingGET"></a>
# **getImageUsingGET**
> ImageEntity getImageUsingGET(id)

根据id获取image

### Example
```java
// Import classes:
ApiExceptApiExceptionControllerAImageControllerApi);
Long id = 789L; // Long | id
try {
    ImageEntity result = apiInstance.getImageUsingGET(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageControllerApi#getImageUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

[**ImageEntity**](ImageEntity.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getImagesByParamUsingGET"></a>
# **getImagesByParamUsingGET**
> ImagePageVO getImagesByParamUsingGET(repoId, repoName, orgId, orgName, appName, tag, page, size)

根据获取image列表

### Example
```java
// Import classes:
//import com.ppdai.dockeryard.client.ApiException;
//import com.ppdai.dockeryard.client.aApiExceptionControllerAImageControllerApi);
Long repoId = 789L; // Long | repoId
String repoName = "repoName_example"; // String | repoName
Long orgId = 789L; // Long | orgId
String orgName = "orgName_example"; // String | orgName
String appName = "appName_example"; // String | appName
String tag = "tag_example"; // String | tag
Integer page = 56; // Integer | page
Integer size = 56; // Integer | size
try {
    ImagePageVO result = apiInstance.getImagesByParamUsingGET(repoId, repoName, orgId, orgName, appName, tag, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImageControllerApi#getImagesByParamUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **repoId** | **Long**| repoId | [optional]
 **repoName** | **String**| repoName | [optional]
 **orgId** | **Long**| orgId | [optional]
 **orgName** | **String**| orgName | [optional]
 **appName** | **String**| appName | [optional]
 **tag** | **String**| tag | [optional]
 **page** | **Integer**| page | [optional]
 **size** | **Integer**| size | [optional]

### Return type

[**ImagePageVO**](ImagePageVO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

