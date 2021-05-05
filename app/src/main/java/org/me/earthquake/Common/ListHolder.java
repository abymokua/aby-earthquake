/**
 * @author Aby Mokua
 * @ID S1732294
 */

package org.me.earthquake.Common;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import org.me.earthquake.R;

public class ListHolder{
    private TextView location;
    private TextView magnitude;
    private TextView date;
    private TextView description;
    private TextView depth;
    private TextView title;
    private boolean titleV;

    public ListHolder(@NonNull View itemView, boolean titleV) {
        location     = itemView.findViewById(R.id.location);
        magnitude = itemView.findViewById(R.id.magnitude);
        date      = itemView.findViewById(R.id.date);
        depth      = itemView.findViewById(R.id.depth);
        description = itemView.findViewById(R.id.description);
        this.titleV = titleV;

        if(titleV){
            title = itemView.findViewById(R.id.title);
        }
    }

    public TextView getLocation() {
        return location;
    }

    public void setLocation(TextView location) {
        this.location = location;
    }

    public TextView getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(TextView magnitude) {
        this.magnitude = magnitude;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }

    public TextView getDepth() {
        return depth;
    }

    public void setDepth(TextView depth) {
        this.depth = depth;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }
}
