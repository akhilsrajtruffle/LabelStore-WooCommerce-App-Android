package com.mavsoft.label;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Models.StoreBasket;
import com.mavsoft.label.Models.StoreItemWithVariation.Variation;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    List<StoreBasket> storeBaskets;
    private ActivityCart context;
    private ItemClickListener mClickListener;

    public CartProductAdapter(List<StoreBasket> storeBaskets, Context context) {
        this.storeBaskets = storeBaskets;
        this.context = (ActivityCart) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_cart,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoreBasket storeBasket = storeBaskets.get(position);

        holder.tvVariationDesc.setVisibility(View.GONE);

        if (storeBasket.getVariationID() != -1) {
            for (Variation storeItemVariation : storeBasket.getStoreItem().getVariations()) {
                if (storeBasket.getVariationID() == storeItemVariation.getId()) {
                    holder.tvVariationDesc.setVisibility(View.VISIBLE);
                    holder.txtProductName.setText(storeBasket.getStoreItem().getName());
                    holder.tvVariationDesc.setText(AWCore.getVariationDescriptionForId(storeItemVariation));
                }
            }
        } else {
            holder.txtProductName.setText(storeBasket.getStoreItem().getName());
        }

        holder.txtProductPrice.setText(AWCore.strToPrice(storeBasket.getStoreItem().getPrice()));
        holder.txtProductQty.setText(Integer.toString(storeBasket.getQty()));

        holder.txtTotal.setText( "Subtotal: " + AWCore.strToPrice(AWCore.woStoreBasketTotal(storeBasket)));

        if (storeBasket.getStoreItem().getImages() != null && storeBasket.getStoreItem().getImages().size() > 0) {
            Glide.with(context)
                    .load(storeBasket.getStoreItem().getImages().get(0).getSrc().replace("https","http"))
                    .into(holder.ivProductImage);
        }
    }

    @Override
    public int getItemCount() {
        return storeBaskets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName;
        public TextView tvVariationDesc;
        public TextView txtProductPrice;
        public TextView txtProductQty;
        public TextView txtTotal;
        public ImageButton btnRemoveFromCart;
        public ImageButton btnQtyAdd;
        public ImageButton btnQtyMinus;
        public ImageView ivProductImage;

        public ViewHolder(View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.tvProductName);
            txtProductPrice = itemView.findViewById(R.id.tvProductPrice);
            txtProductQty = itemView.findViewById(R.id.tvProductQty);
            txtTotal = itemView.findViewById(R.id.tvTotal);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            btnRemoveFromCart = itemView.findViewById(R.id.btnRemoveFromCart);
            btnQtyAdd = itemView.findViewById(R.id.btnQtyAdd);
            btnQtyMinus = itemView.findViewById(R.id.btnQtyMinus);
            tvVariationDesc = itemView.findViewById(R.id.tvVariationDesc);

            btnRemoveFromCart.setOnClickListener(this);
            btnQtyAdd.setOnClickListener(this);
            btnQtyMinus.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (view.equals(btnQtyAdd)) {

                if (Paper.book().getAllKeys().contains("basket")) {
                    List<StoreBasket> storeBasket = Paper.book().read("basket");

                    int currentQty = storeBasket.get(getAdapterPosition()).getQty();

                    storeBasket.get(getAdapterPosition()).setQty(currentQty + 1);

                    Paper.book().write("basket", storeBasket);

                    context.storeBasket = AWCore.getBasket();
                    context.reloadAdapter();
                    context.adapter.notifyDataSetChanged();
                }

            } else if (view.equals(btnQtyMinus)) {
                List<StoreBasket> storeBasket = Paper.book().read("basket");

                int currentQty = storeBasket.get(getAdapterPosition()).getQty();

                if ((currentQty - 1) <= 0) {
                    currentQty = 1;
                } else {
                    currentQty = currentQty - 1;
                }
                storeBasket.get(getAdapterPosition()).setQty((currentQty));

                Paper.book().write("basket", storeBasket);

                context.storeBasket = AWCore.getBasket();
                context.reloadAdapter();
                context.adapter.notifyDataSetChanged();
            } else if (view.equals(btnRemoveFromCart)) {

                    List<StoreBasket> storeBasket = new ArrayList<StoreBasket>();
                    storeBasket = AWCore.getBasket();
                    storeBasket.remove(getAdapterPosition());

                    Paper.book().write("basket",storeBasket);

                context.storeBasket = AWCore.getBasket();
                context.reloadAdapter();
                context.adapter.notifyDataSetChanged();
            }

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
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