/*
 *  Copyright (c) 2017 - present, Xuan Wang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.ucsb.cs.cs190i.papertown.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment
    implements View.OnClickListener{

  private OnClickListener mOnClickListener = null;
  TextView titleView;
  TextView descriptionView;

  public interface OnClickListener {
    void onClick(View view);
  }

  public ItemFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View rootView = inflater.inflate(R.layout.fragment_item, container, false);
    Bundle args = getArguments();

    String title = args.getString("title", "");
    titleView = (TextView) rootView.findViewById(R.id.text_title);
    titleView.setText(title);
    titleView.setOnClickListener(this);

    String description = args.getString("desc", "");
    descriptionView = (TextView) rootView.findViewById(R.id.text_subtitle);
    descriptionView.setText(description);
    return rootView;
  }

  @Override
  public void onClick(View view) {
    if (mOnClickListener != null) {
      // get tag
      mOnClickListener.onClick(view);
    }
  }

  public void setOnClickListener(OnClickListener listener) {
    this.mOnClickListener = listener;
  }
}
