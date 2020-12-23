package me.kostevych.widgets;

import me.kostevych.widgets.model.Widget;
import me.kostevych.widgets.model.WidgetRequest;
import me.kostevych.widgets.services.WidgetStoreService;
import me.kostevych.widgets.services.WidgetStoreServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@SpringBootTest
class WidgetsStoreTests {

	private static final int RANDOM_MIN = 10;
	private static final int RANDOM_MAX = 100;

	WidgetStoreService store = new WidgetStoreServiceImpl();
	@BeforeEach
	void clear() {
		for(Widget widget : store.getAll()) {
			store.delete(widget.getId());
		}
	}

	@Test
	void addWidgets() {
		for(int i = 0; i < 10; i++) {
			store.add(mockedWidget());
		}

		assert 10 == store.getAll().size();
	}

	@Test
	void makeSureZisAlwaysUniq() {
		for(int i = 0; i < 10; i++) {
			store.add(mockedWidget(0));
		}

		Set<Integer> zIndexes = new HashSet<>();
		for(Widget widget : store.getAll()) {
			zIndexes.add(widget.getZ());
		}

		Assertions.assertEquals(10, zIndexes.size());
	}

	@Test
	void delete() {
		Widget added = store.add(mockedWidget());

		assert store.delete(added.getId());
	}

	@Test
	void editWithNewZindex() {
		for(int i = 0; i < 3; i++) {
			store.add(mockedWidget(i));
		}

		int z = store.getAll().get(2).getZ();

		WidgetRequest editRequest = new WidgetRequest();
		editRequest.setWidth(5);
		editRequest.setHeight(5);
		editRequest.setY(10);
		editRequest.setX(10);
		editRequest.setZ(z); //as the last one
		Widget editedWidget = store.edit(store.getAll().get(1).getId(), editRequest);

		Assertions.assertAll(
				() -> Assertions.assertEquals(5, editedWidget.getWidth()),
				() -> Assertions.assertEquals(5, editedWidget.getHeight()),
				() -> Assertions.assertEquals(10, editedWidget.getX()),
				() -> Assertions.assertEquals(10, editedWidget.getY()),
				() -> Assertions.assertEquals(z, editedWidget.getZ()), //as old max wal
				() -> Assertions.assertEquals(z + 1, store.getAll().get(2).getZ()) //old one should be incremented
		);
	}

	@Test
	void gitSingleItem() {
		Widget storedWidget = store.add(mockedWidget());

		Widget widget = store.get(storedWidget.getId());

		assert storedWidget.equals(widget);
	}

	WidgetRequest mockedWidget() {
		Random r = new Random();
		WidgetRequest request = new WidgetRequest();
		request.setWidth(r.nextInt(RANDOM_MAX-RANDOM_MIN) + RANDOM_MIN);
		request.setHeight(r.nextInt(RANDOM_MAX-RANDOM_MIN) + RANDOM_MIN);
		request.setY(r.nextInt(RANDOM_MAX-RANDOM_MIN) + RANDOM_MIN);
		request.setX(r.nextInt(RANDOM_MAX-RANDOM_MIN) + RANDOM_MIN);
		request.setZ(r.nextInt(RANDOM_MAX-RANDOM_MIN));

		return request;
	}

	WidgetRequest mockedWidget(int z) {
		WidgetRequest request = mockedWidget();
		request.setZ(z);

		return request;
	}

}
