package org.gaochun.dexlibrary1;

import android.content.Context;
import android.widget.Toast;

/**
 * 动态加载测试实现(这里仅仅是一个示例)
 */
public class ShowMessageImpl_one implements IMessage_one {
    @Override
    public String showMessage(Context context) {
        Toast.makeText(context, "dexlibrary1生成的dex文件已加载", Toast.LENGTH_LONG).show();
        return "大家好才是真的好";
    }
}
