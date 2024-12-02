package com.inhatc.real_project.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Category.class, TodoItem.class}, version = 4, exportSchema = false) // 버전 4로 업데이트
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static void destroyInstance() {

    }

    public abstract CategoryDao categoryDao();
    public abstract TodoItemDao todoItemDao();

    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4) // 마이그레이션 추가
                    .fallbackToDestructiveMigration() // 개발 단계에서만 사용
                    .build();
        }
        return instance;
    }

    // 기존 마이그레이션: (1 -> 2)
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE categories ADD COLUMN description TEXT DEFAULT ''");
        }
    };

    // 기존 마이그레이션: (2 -> 3)
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 스키마 변경이 없는 경우
        }
    };

    // 새 마이그레이션: (3 -> 4)
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 새 테이블 생성
            database.execSQL("CREATE TABLE IF NOT EXISTS todoitem_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "categoryId INTEGER NOT NULL, " +
                    "itemName TEXT NOT NULL)");

            // 기존 데이터 복사
            database.execSQL("INSERT INTO todoitem_new (id, categoryId, itemName) " +
                    "SELECT id, categoryId, itemName FROM todoitem");

            // 기존 테이블 삭제 및 새 테이블 이름 변경
            database.execSQL("DROP TABLE todoitem");
            database.execSQL("ALTER TABLE todoitem_new RENAME TO todoitem");
        }
    };
}
