package com.example.contacts;
import androidx.room.Room;

import com.example.contacts.database.AppDatabase;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addingToDatabase() {
        assertEquals(4, 2 + 2);
    }
}