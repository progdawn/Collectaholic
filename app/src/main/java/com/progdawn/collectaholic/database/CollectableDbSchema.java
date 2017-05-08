package com.progdawn.collectaholic.database;

/**
 * Created by Dawn Myers on 5/7/2017.
 */

public class CollectableDbSchema {

    public static final class CollectableTable{
        public static final String NAME = "collectables";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String SERIES = "series";
            public static final String DATE = "date";
        }
    }
}
