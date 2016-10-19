package com.example.yddchsc.baidumap;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

class ListViewAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private List<friend> listItems;    //日记信息集合
    private LayoutInflater listContainer;           //视图容器

    public final class ListItemView{                //自定义控件集合
        public TextView name_cell;
        public Button delete_button_cell;
    }

    public ListViewAdapter(Context context, List<friend> listItems) {
        this.context = context;
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文
        this.listItems = listItems;
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getItemViewType(int position){
        friend bean = listItems.get(position);
        return bean.getType();
    }
    @Override
    public int getViewTypeCount(){
        return 2 ;
    }

    /**
     * ListView Item设置
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        //自定义视图
        ListItemView  listItemView = null;
        if (convertView != null) {
            listItemView = (ListItemView) convertView.getTag();
            return convertView;
        }else{
                listItemView = new ListItemView();
                //获取list_item布局文件的视图
                convertView = listContainer.inflate(R.layout.friends_list_item, null);
        }

        //获取控件对象
        listItemView.name_cell = (TextView) convertView.findViewById(R.id.name_cell);
        listItemView.delete_button_cell = (Button) convertView.findViewById(R.id.delete_button_cell);
        //设置控件集到convertView
        convertView.setTag(listItemView);

        //注册按钮点击时间爱你
       /* listItemView.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        listItemView.delete_button_cell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        return convertView;
    }

}