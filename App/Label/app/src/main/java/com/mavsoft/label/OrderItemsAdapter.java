package com.mavsoft.label;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Models.StoreBasket;

import java.util.List;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {

    List<StoreBasket> storeBaskets;
    private Context context;

    public OrderItemsAdapter(List<StoreBasket> storeBaskets, Context context) {
        this.storeBaskets = storeBaskets;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_order_confirmed,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoreBasket storeBasket = storeBaskets.get(position);

        holder.txtTitle.setText(storeBasket.getStoreItem().getName());
        holder.txtDescription.setText(Integer.toString(storeBasket.getQty()) + " | " +  AWCore.strToPrice(storeBasket.getStoreItem().getPrice()));
        Glide.with(context)
                .load(storeBasket.getStoreItem().getImages().get(0).getSrc().replace("https","http"))
                .into(holder.ivOrderImage);
    }

    @Override
    public int getItemCount() {
        return storeBaskets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTitle;
        public TextView txtDescription;
        public ImageView ivOrderImage;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.tvOrderTitle);
            txtDescription = (TextView) itemView.findViewById(R.id.tvOrderDescription);
            ivOrderImage = (ImageView) itemView.findViewById(R.id.ivOrderImage);
        }
    }
}
