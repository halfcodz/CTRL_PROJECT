package com.halfcodz.ctrl_project.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Control.class, TodoItem.class, Focus.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance; // 싱글톤 패턴

    public abstract ControlDao controlDao();
    public abstract TodoItemDao todoItemDao();
    public abstract FocusDao focusDao();

    // 싱글톤 데이터베이스 인스턴스를 반환
    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigration() // 데이터 손실 허용 (개발 단계에서만)
                    .build();
        }
        return instance;
    }

    // 1 -> 2로의 마이그레이션 정의
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 기존 테이블 삭제
            database.execSQL("DROP TABLE IF EXISTS `todoitem`");

            // 새로운 스키마로 테이블 생성
            database.execSQL("CREATE TABLE IF NOT EXISTS `todoitem` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`title` TEXT, " +
                    "`start_sch` TEXT, " +
                    "`end_sch` TEXT)");
        }
    };
    // 2 -> 3으로의 마이그레이션 정의
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Control 테이블에 `isChecked` 컬럼 추가 (체크박스 상태 추가)
            database.execSQL("ALTER TABLE controls ADD COLUMN isChecked INTEGER DEFAULT 0 NOT NULL");
        }
    };



    public static void destroyInstance() {

    }

}