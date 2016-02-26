package com.basket.basketmanager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.basket.basketmanager.model.AppData;
import com.basket.basketmanager.model.StopOver;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItineraryAdapter extends BaseAdapter {

    private List<StopOver> mItinerary;
    private View itemView;
    private Context mContext;

    public ItineraryAdapter(List<StopOver> list, Context context) {
        mItinerary = list;
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mItinerary.size();
    }

    @Override
    public Object getItem(int position) {
        return mItinerary.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        StopOverViewHolder viewHolder;
        if (view == null) {
            viewHolder = onCreateViewHolder(viewGroup, i);
            view = itemView;
            view.setTag(viewHolder);
        } else {
            viewHolder = (StopOverViewHolder) view.getTag();
            viewHolder.transportText.setText(null);
            viewHolder.previousColor.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.none));
            viewHolder.nextColor.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.none));
        }

        final SwipeLayout swipeLayout = (SwipeLayout) view;
        swipeLayout.setFocusable(true);
        swipeLayout.setFocusableInTouchMode(true);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                layout.requestFocus();
            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                layout.clearFocus();
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        swipeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                    swipeLayout.close();
                    mItinerary.remove(i);
                    ItineraryAdapter.this.notifyDataSetChanged();
                }
            }
        });

        View surface = swipeLayout.getSurfaceView();
        int size = mItinerary.size();

        if (i == 0 || i == size - 1) {
            surface.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            swipeLayout.setRightSwipeEnabled(false);

            if (i == size - 1) {
                surface.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                surface.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setFocusableInTouchMode(true);
                        v.requestFocus();
                        v.clearFocus();
                        v.setFocusableInTouchMode(false);
                    }
                });
            }
        }
        if (i != size - 1) {
            surface.setClickable(true);

            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
            surface.setBackgroundResource(typedValue.resourceId);

            surface.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StopOver stopover = mItinerary.get(i);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    v.clearFocus();
                    v.setFocusableInTouchMode(false);
                    ItineraryAdapter.this.editStopOver(mItinerary.indexOf(stopover));
                }
            });

            if (i != 0) {
                surface.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
                swipeLayout.setRightSwipeEnabled(true);
            }
        }

        View deleted = view.findViewById(R.id.deleted);
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
            }
        });

        View cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeLayout.close();
            }
        });

        onBindViewHolder(viewHolder, i);
        return view;
    }

    public void editStopOver(int position) {
        editStopOverDialog(position).show(((Activity) getContext()).getFragmentManager());
    }

    public StopOverDialog editStopOverDialog(int position) {
        final StopOver stopover = mItinerary.get(position);
        StopOverDialog stopOverDialog = new StopOverDialog();
        stopOverDialog.setTitle("Modifica tappa");
        stopOverDialog.setText(stopover.getPlace());
        stopOverDialog.setSpinnerPosition(stopover.getTransport());
        stopOverDialog.setDriverChecked(stopover.isDriver());

        if (position == 0) {
            stopOverDialog.setTextViewEnabled(false);
        }

        stopOverDialog.setListener(new StopOverDialog.StopOverDialogListener() {
            @Override
            public void onStopOverDialogOkButton(StopOverDialog dialog) {
                String previous = stopover.getPlace();
                if (!previous.equals(dialog.getText())) {
                    if (errors(dialog)) {
                        return;
                    }
                }
                stopover.setPlace(dialog.getText());
                stopover.setTransport(dialog.getSpinnerPosition());
                stopover.setDriver(dialog.isDriverChecked());
                ItineraryAdapter.this.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        return stopOverDialog;
    }

    public void addNewStopOver() {
        StopOverDialog dialog = new StopOverDialog();
        dialog.setTitle("Nuova tappa");
        dialog.setListener(new StopOverDialog.StopOverDialogListener() {
            @Override
            public void onStopOverDialogOkButton(StopOverDialog dialog) {
                if (errors(dialog)) {
                    return;
                }
                int index = mItinerary.size() - 1;
                if (index < 0) {
                    index = 0;
                }
                mItinerary.add(index, new StopOver(dialog.getText().trim(), dialog.getSpinnerPosition(), dialog.isDriverChecked()));
                ItineraryAdapter.this.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show(((Activity) getContext()).getFragmentManager());
    }

    private boolean errors(StopOverDialog dialog) {
        AutoCompleteTextView stopOver = dialog.getTextView();

        String text = dialog.getText();
        if (text == null) {
            return true;
        }

        text = text.trim();
        if (text.equals("")) {
            stopOver.setError("Inserisci il nome della città");
            stopOver.requestFocus();
            return true;
        }

        if (isPlanned(text)) {
            stopOver.setError("Tappa già presente");
            stopOver.requestFocus();
            return true;
        }

        if (!isValid(text)) {
            stopOver.setError("Non conosco questa città");
            stopOver.requestFocus();
            return true;
        }

        return false;
    }

    private boolean isPlanned(String text) {
        if (text == null) {
            return false;
        }

        String stopover = text.trim();
        for (StopOver current : mItinerary) {
            if (current.getPlace().trim().equalsIgnoreCase(stopover)) {
                return true;
            }
        }

        return stopover.equalsIgnoreCase(AppData.FIRST_REFEREE.getPlace());
    }

    private boolean isValid(String text) {
        if (text == null) {
            return false;
        }

        String stopover = text.trim();

        for (String current : AppData.PLACES) {
            if (stopover.equalsIgnoreCase(current)) {
                return true;
            }
        }

        return false;
    }

    public void onBindViewHolder(StopOverViewHolder viewHolder, int i) {
        viewHolder.place.setText(mItinerary.get(i).getPlace());

        int[] colors = getContext().getResources().getIntArray(R.array.colors);

        List<String> transportsList = new ArrayList<>(
                Arrays.asList(getContext().getResources().getStringArray(R.array.transport_array)));

        transportsList.add("Viaggio con un collega");

        StopOver stopOver = mItinerary.get(i);
        int nextTransport = stopOver.getTransport();
        if (nextTransport == 0 && !stopOver.isDriver()) {
            nextTransport = transportsList.size() - 1;
        }

        if (nextTransport >= 0) {
            viewHolder.nextColor.setBackgroundColor(colors[nextTransport]);
            viewHolder.transportText.setText(transportsList.get(nextTransport));
            if (i == 0) {
                viewHolder.previousColor.setBackgroundColor(colors[nextTransport]);
            }
        }
        if (i > 0) {
            StopOver previous = mItinerary.get(i - 1);
            int previousTransport = previous.getTransport();
            if (previousTransport == 0 && !previous.isDriver()) {
                previousTransport = transportsList.size() - 1;
            }

            if (previousTransport >= 0) {
                viewHolder.previousColor.setBackgroundColor(colors[previousTransport]);
                if (i == getCount() - 1) {
                    viewHolder.nextColor.setBackgroundColor(colors[previousTransport]);
                }
            }

            if (nextTransport == previousTransport) {
                viewHolder.transportText.setText(null);
            }
        }
    }

    public StopOverViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itinerary_item, viewGroup, false);
        return new StopOverViewHolder(itemView);
    }

    public static class StopOverViewHolder extends RecyclerView.ViewHolder {
        TextView place;
        TextView transportText;
        View previousColor;
        View nextColor;

        public StopOverViewHolder(View view) {
            super(view);
            place = (TextView) view.findViewById(R.id.stopover);
            transportText = (TextView) view.findViewById(R.id.transport_text);
            previousColor = view.findViewById(R.id.color_previous);
            nextColor = view.findViewById(R.id.color_next);
        }
    }
}



/***
 surface.setOnLongClickListener(new View.OnLongClickListener() {
@Override
public boolean onLongClick(final View v) {
new DialogFragment() {
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
final TextView stopover = (TextView) v.findViewById(R.id.stopover);
builder.setTitle(stopover.getText())
.setItems(R.array.context_menu, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
switch (which) {
case 0:
StopOverDialog stopOverDialog = new StopOverDialog();
stopOverDialog.setText(stopover.getText().toString());
stopOverDialog.setListener(new StopOverDialog.StopOverDialogListener() {
@Override
public void onStopOverDialogOkButton(StopOverDialog dialog) {
stopover.setText(dialog.getText());
}

@Override
public void onStopOverDialogCancelButton(StopOverDialog dialog) {

}
});
stopOverDialog.show(getFragmentManager(), StopOverDialog.TAG);
break;
case 1:
SwipeLayout swipeLayout = (SwipeLayout) v.getParent();
swipeLayout.open();
break;
}
}
});

return builder.create();
}
}.show(((Activity) getContext()).getFragmentManager(), "");
return true;
}
});
 */
