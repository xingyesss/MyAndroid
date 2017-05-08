package com.xingye.myandroid.callback;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.xingye.myandroid.utils.GsonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sunxiquan on 2017/5/8.
 */

public abstract class JsonCallback<T> extends AbsCallback<T> {
    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
        request.headers(new HttpHeaders());
        request.params(new HttpParams());
    }

    /**
     * 添加缓存时，javaBean对象必须实现Serializable接口
     * @param t
     * @param call
     */
    @Override
    public void onCacheSuccess(T t, Call call) {
        super.onCacheSuccess(t, call);
    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        //以下代码是通过泛型解析实际参数,泛型必须传

        //com.lzy.demo.callback.DialogCallback<com.lzy.demo.model.Login> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        //我们的示例代码中，只有一个泛型，所以取出第一个，得到如下结果
        //com.lzy.demo.model.Login
        Type type = params[0];

        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        //有数据类型，表示有data
        T data = GsonUtils.toBean(jsonReader, type);
        response.close();
        return data;
    }
}
