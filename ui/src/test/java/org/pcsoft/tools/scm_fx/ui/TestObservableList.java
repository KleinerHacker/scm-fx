package org.pcsoft.tools.scm_fx.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by pfeifchr on 03.11.2014.
 */
public class TestObservableList {

    public static final class TestObject {
        private final ReadOnlyStringWrapper title = new ReadOnlyStringWrapper();
        private final BooleanProperty selected = new SimpleBooleanProperty(false);

        public TestObject(String title) {
            this.title.set(title);
        }

        public String getTitle() {
            return title.get();
        }

        public ReadOnlyStringProperty titleProperty() {
            return title.getReadOnlyProperty();
        }

        public boolean getSelected() {
            return selected.get();
        }

        public BooleanProperty selectedProperty() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title.get());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final TestObject other = (TestObject) obj;
            return Objects.equals(this.title.get(), other.title.get());
        }

        @Override
        public String toString() {
            return "TestObject{" +
                    "title=" + title.get() +
                    ", selected=" + selected.get() +
                    '}';
        }
    }

    public static final class TestProperty extends SimpleObjectProperty<TestObject> {
        public TestProperty(String name) {
            super(new TestObject(name));
            init();
        }

        public TestProperty(TestObject testObject) {
            super(testObject);
            init();
        }

        public String getTitle() {
            return getValue().getTitle();
        }

        public void setSelected(boolean selected) {
            getValue().setSelected(selected);
        }

        public boolean getSelected() {
            return getValue().getSelected();
        }

        public BooleanProperty selectedProperty() {
            return getValue().selectedProperty();
        }

        public ReadOnlyStringProperty titleProperty() {
            return getValue().titleProperty();
        }

        @Override
        public void set(TestObject testObject) {
            super.set(testObject);
            init();
        }

        @Override
        public void setValue(TestObject testObject) {
            super.setValue(testObject);
            init();
        }

        private void init() {
            if (get() == null)
                return;

            get().titleProperty().addListener((v, o, n) -> fireValueChangedEvent());
            get().selectedProperty().addListener((v, o, n) -> {
                fireValueChangedEvent();
            });
        }
    }

    @Test
    public void testSimple() {
        final AtomicInteger counter = new AtomicInteger(0);
        final TestProperty testProperty = new TestProperty("Test");
        testProperty.addListener(observable -> {
            System.out.println("New state: " + testProperty.get().toString());
            counter.incrementAndGet();
        });

        testProperty.setSelected(true);
        testProperty.setSelected(false);

        Assert.assertEquals(2, counter.intValue());
    }

    @Test
    public void testList() {
        final AtomicInteger counter = new AtomicInteger(0);
        final ObservableList<TestObject> observableList = new ObservableListWrapper<>(new ArrayList<>(), new Callback<TestObject, Observable[]>() {
            @Override
            public Observable[] call(TestObject o) {
                return new Observable[] {
                        new TestProperty(o)
                };
            }
        });
        observableList.add(new TestObject("Test 1"));
        observableList.add(new TestObject("Test 2"));
        observableList.add(new TestObject("Test 3"));

        observableList.addListener((Observable observable) -> {
            System.out.println("New state: " + observable);
            counter.incrementAndGet();
        });

        observableList.get(1).setSelected(true);
        observableList.get(2).setSelected(true);
        observableList.get(1).setSelected(false);
        observableList.get(2).setSelected(false);

        Assert.assertEquals(4, counter.intValue());
    }

}
