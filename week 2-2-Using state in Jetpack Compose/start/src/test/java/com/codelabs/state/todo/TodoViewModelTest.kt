/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import com.codelabs.state.util.generateRandomTodoItem
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.IllegalArgumentException

class TodoViewModelTest {
    @Test
	fun whenRemoveItem_updateList() {
		val viewModel = TodoViewModel()
		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()
		viewModel.addItem(item1)
		viewModel.addItem(item2)

		viewModel.removeItem(item1)

		assertThat(viewModel.todoItems).isEqualTo(listOf(item2))
	}

	@Test
	fun whenAddItem_updateList() {
		val viewModel = TodoViewModel()

		assertThat(viewModel.todoItems).isEqualTo(emptyList<TodoItem>())

		val item1 = generateRandomTodoItem()
		viewModel.addItem(item1)

		assertThat(viewModel.todoItems).isEqualTo(listOf(item1))
	}

	@Test
	fun onEditItemSelected_updateCurrentEditPosition() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()
		val item3 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)
		viewModel.addItem(item3)

		assertTrue(viewModel.currentEditPosition == -1)

		viewModel.onEditItemSelected(item2)

		assertTrue(viewModel.currentEditPosition == viewModel.todoItems.indexOf(item2))
	}

	@Test
	fun onEditDone_updateCurrentEditPosition() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()
		val item3 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)
		viewModel.addItem(item3)

		viewModel.onEditItemSelected(item2)

		viewModel.onEditDone()

		assertTrue(viewModel.currentEditPosition == -1)
	}

	@Test
	fun onEditItemChange_updateList() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()
		val item3 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)
		viewModel.addItem(item3)

		viewModel.onEditItemSelected(item2)

		val newItem2 = item2.copy(task = "Test Task")

		viewModel.onEditItemChange(newItem2)

		assertThat(viewModel.currentEditItem).isEqualTo(newItem2)
	}

	@Test
	fun whenNotEditing_currentEditItemIsNull() {
		val viewModel = TodoViewModel()

		val item = generateRandomTodoItem()
		viewModel.addItem(item)

		assertThat(viewModel.currentEditItem).isNull()
	}

	@Test
	fun whenEditing_currentEditItemIsCorrectItem() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()
		val item3 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)
		viewModel.addItem(item3)

		viewModel.onEditItemSelected(item2)

		assertThat(viewModel.currentEditItem).isEqualTo(item2)
	}

	@Test
	fun whenEditingDone_currentEditItemIsCorrectItem() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)

		viewModel.onEditItemSelected(item2)

		viewModel.onEditDone()

		assertThat(viewModel.currentEditItem).isNull()
	}

	@Test
	fun whenEditingItem_updateAreShownInItemAndList() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)

		viewModel.onEditItemSelected(item2)

		val newItem2 = item2.copy(task = "Test Task")

		viewModel.onEditItemChange(newItem2)

		assertThat(viewModel.currentEditItem).isEqualTo(newItem2)
		assertThat(viewModel.todoItems).isEqualTo(listOf(item1, newItem2))
	}

	@Test(expected = IllegalArgumentException::class)
	fun whenEditing_wrongItemThrows() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()
		val item2 = generateRandomTodoItem()

		viewModel.addItem(item1)
		viewModel.addItem(item2)

		viewModel.onEditItemSelected(item2)

		val newItem1 = item1.copy(task = "Test Task")

		viewModel.onEditItemChange(newItem1)
	}

	@Test(expected = IllegalArgumentException::class)
	fun whenNoEditing_onEditItemChangeThrows() {
		val viewModel = TodoViewModel()

		val item1 = generateRandomTodoItem()

		viewModel.addItem(item1)

		viewModel.onEditItemChange(item1)
	}
}
