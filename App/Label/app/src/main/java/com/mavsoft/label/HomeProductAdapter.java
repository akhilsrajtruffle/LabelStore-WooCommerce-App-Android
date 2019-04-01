package com.mavsoft.label;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Helpers.IntentHelper;
import com.mavsoft.label.Models.StoreItem;
import java.util.List;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {

    List<StoreItem> storeItems;
    private Context context;
    private ItemClickListener mClickListener;

    public HomeProductAdapter(List<StoreItem> storeItems, Context context) {
        this.storeItems = storeItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_home_products,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoreItem storeItem = storeItems.get(position);

        String price = storeItem.getPrice();

        if (price != null) {
            holder.txtPrice.setText(AWCore.strToPrice(price));
        }
        holder.txtTitle.setText(storeItem.getName());

        Glide.with(context)
                .load(storeItem.getImages().get(0).getSrc().replace("https","http"))
                .into(holder.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return storeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtTitle;
        public TextView txtPrice;
        public ImageView ivProductImage;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.tvHomeProductTitle);
            txtPrice = (TextView) itemView.findViewById(R.id.tvHomeProductPrice);
            ivProductImage = (ImageView) itemView.findViewById(R.id.ivHomeProductImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            StoreItem item = storeItems.get(getAdapterPosition());

            if (item.getType().equals("variable")) {

                Intent i = new Intent(context, ActivityProductFashionDetail.class);

                IntentHelper.addObjectForKey(item, "storeItem");

                context.startActivity(i);

            } else {

                Intent i = new Intent(context, ActivityProductDetail.class);

                IntentHelper.addObjectForKey(item, "storeItem");

                context.startActivity(i);

            }
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return storeItems.get(id).getId().toString();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}