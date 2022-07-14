/**
 * Shire
 * https://github.com/sangupta/shire
 *
 * MIT License.
 * Copyright (c) 2022, Sandeep Gupta.
 *
 * Use of this source code is governed by a MIT style license
 * that can be found in LICENSE file in the code repository.
 */

package utils

func RemoveFromSliceAtIndex[T any](slice []T, index int) []T {
	if (slice == nil) || (index < 0) || (index >= len(slice)) {
		// do nothing
		return slice
	}

	size := len(slice)
	slice[index] = slice[size-1]
	return slice[:size-1]
}
