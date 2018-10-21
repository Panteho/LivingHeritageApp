package com.people_patterns.livingheritage;

import com.people_patterns.livingheritage.model.Tree;

import java.util.Collection;

public interface OnGetTrees {
    void onTreesFetched(Collection<Tree> values);
}
