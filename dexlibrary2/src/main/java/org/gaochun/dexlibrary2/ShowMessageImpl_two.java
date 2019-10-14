package org.gaochun.dexlibrary2;

import android.content.Context;
import android.widget.Toast;

/**
 * 动态加载测试实现
 */
public class ShowMessageImpl_two implements IMessage_two {
    @Override
    public String showMessage(Context context) {
        Toast.makeText(context, "dexlibrary2生成的dex文件已加载", Toast.LENGTH_LONG).show();
        return "大家好才是真的好 0.0";
    }
}
