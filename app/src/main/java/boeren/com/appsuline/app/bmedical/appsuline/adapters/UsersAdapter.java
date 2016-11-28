package boeren.com.appsuline.app.bmedical.appsuline.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.io.File;
import java.util.ArrayList;

import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;
import boeren.com.appsuline.app.bmedical.appsuline.viewholders.UserViewHolder;

public class UsersAdapter extends BaseListAdapter<User> implements CompoundButton.OnCheckedChangeListener{
    private User activeUser;
    public UsersAdapter(Context context, ArrayList<User> objects) {
        super(context, objects);
    }

    @Override
    public View inflateView(LayoutInflater inflater, int position, ViewGroup parent) {
        return inflater.inflate(R.layout.row_user,null);
    }

    @Override
    public Object getViewHolder(View convertView, int position) {
        return new UserViewHolder(convertView);
    }

    @Override
    public void populateView(Object viewHolder, int position) {
        UserViewHolder userViewHolder=(UserViewHolder)viewHolder;
        User user=getItem(position);

        if(user.isActiveUser()){
            activeUser=user;
        }
        userViewHolder.getTitle().setText(user.getName());
        //userViewHolder.getDescription().setText(user.getName());

        userViewHolder.getIsActive().setOnCheckedChangeListener(null);
        userViewHolder.getIsActive().setChecked(user.isActiveUser());
        userViewHolder.getIsActive().setOnCheckedChangeListener(this);
        userViewHolder.getIsActive().setTag(position);
        if(null!=user.getImageSource() && !user.getImageSource().isEmpty()){
            File f = new File(user.getImageSource());
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            userViewHolder.getImage().setImageBitmap(bmp);
        }else{
            userViewHolder.getImage().setImageResource(R.drawable.ic_nopic);
        }


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


        Integer currentlySelectedPosition=(Integer)buttonView.getTag();
        User currentSelected=getItem(currentlySelectedPosition);
        currentSelected.setActiveUser(true);
        if(null!=activeUser && activeUser.getUserId()!=currentSelected.getUserId()){
            activeUser.setActiveUser(false);
            BaseController.getInstance().getDbManager(getContext()).getUserTable().update(activeUser);
        }
        BaseController.getInstance().getDbManager(getContext()).getUserTable().update(currentSelected);
        BaseController.getInstance().setActiveUser(currentSelected);

        notifyDataSetChanged();
    }
}
