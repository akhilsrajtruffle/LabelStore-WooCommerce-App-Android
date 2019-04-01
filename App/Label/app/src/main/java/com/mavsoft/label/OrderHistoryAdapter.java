package com.mavsoft.label;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mavsoft.label.Helpers.AWCore;
import com.mavsoft.label.Models.StoreOrderHistory.StoreOrderHistory;
import java.util.ArrayList;
import io.paperdb.Paper;

/**
 * Created by Anthony Gordon on 10/09/2017.
 */

//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    StoreOrderHistory[] storeOrderHistory;
    private ActivityOrders context;

    public OrderHistoryAdapter(StoreOrderHistory[] storeOrderHistory, Context context) {
        this.storeOrderHistory = storeOrderHistory;
        this.context = (ActivityOrders) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_orders,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StoreOrderHistory storeOrderHistory = this.storeOrderHistory[position];

        holder.tvItemDesc.setText(storeOrderHistory.getItemNames());
        holder.tvAddress.setText(storeOrderHistory.getCustAddress());
        holder.tvOrderDate.setText("Date: " + storeOrderHistory.getDateCreated());
        holder.tvOrderRef.setText("Order Ref #" + storeOrderHistory.getId());
        holder.tvOrderSubtotal.setText("Subtotal: " + AWCore.strToPrice(String.valueOf(storeOrderHistory.getItemSubtotal())));
        holder.tvOrderTotal.setText("Total: " + AWCore.strToPrice(String.valueOf(storeOrderHistory.getOrderTotal())));
        holder.tvName.setText(storeOrderHistory.getCustName());
    }

    @Override
    public int getItemCount() {
        return storeOrderHistory.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvItemDesc;
        public TextView tvOrderDate;
        public TextView tvOrderSubtotal;
        public TextView tvOrderTotal;
        public TextView tvOrderRef;
        public TextView tvName;
        public TextView tvAddress;


        public ViewHolder(View itemView) {
            super(itemView);

            tvItemDesc = (TextView) itemView.findViewById(R.id.tvItemDesc);
            tvOrderDate = (TextView) itemView.findViewById(R.id.tvOrderDate);
            tvOrderSubtotal = (TextView) itemView.findViewById(R.id.tvOrderSubtotal);
            tvOrderTotal = (TextView) itemView.findViewById(R.id.tvOrderTotal);
            tvOrderRef = (TextView) itemView.findViewById(R.id.tvOrderRef);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Order: #" + storeOrderHistory[getAdapterPosition()].getId())
                    .setMessage("Choose an action")
                    .setPositiveButton("Hide Order", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            ArrayList<Integer> orders = Paper.book().read("Orders");
                            orders.remove(getAdapterPosition());
                            Paper.book().write("Orders",orders);

                            context.apiGetOrders();
                            context.adapter.notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

}
