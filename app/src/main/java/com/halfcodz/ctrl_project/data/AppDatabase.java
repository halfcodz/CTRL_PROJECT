package com.halfcodz.ctrl_project.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Control.class, TodoItem.class, Focus.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;

    public abstract ControlDao controlDao();
    public abstract TodoItemDao todoItemDao();
    public abstract FocusDao focusDao();

    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .fallbackToDestructiveMigration() // 개발 중 데이터베이스 재생성
                    .build();
        }
        return instance;
    }


    // 1 -> 2 마이그레이션: `todoitem` 테이블 재생성
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS `todoitem`");
            database.execSQL("CREATE TABLE IF NOT EXISTS `todoitem` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT, " +
                    "`start_sch` TEXT, " +
                    "`end_sch` TEXT)");
        }
    };

    // 2 -> 3 마이그레이션: `controls` 테이블에 `is_checked` 컬럼 추가
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE controls ADD COLUMN is_checked INTEGER DEFAULT 0 NOT NULL");
        }
    };

    // 3 -> 4 마이그레이션: `focus` 테이블 생성
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `focus` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`focus_name` TEXT, " +
                    "`description` TEXT)");
        }
    };

    // 4 -> 5 마이그레이션: `controls` 테이블에 `new_column` 컬럼 추가
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE controls RENAME COLUMN categoryId TO category_id");
            database.execSQL("ALTER TABLE controls RENAME COLUMN Category_Name TO category_name");
            database.execSQL("ALTER TABLE controls RENAME COLUMN Control_Item TO control_item");
            database.execSQL("ALTER TABLE controls RENAME COLUMN isChecked TO is_checked");
        }
    };

    public static void destroyInstance() {
        instance = null;
    }
}