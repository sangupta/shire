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

package logger

import "fmt"

func Debug(message string) {
	fmt.Println("DEBUG: " + message)
}

func Info(message string) {
	fmt.Println("INFO: " + message)
}

func Warn(message string) {
	fmt.Println("WARN: " + message)
}

func Error(message string) {
	fmt.Println("ERROR: " + message)
}

func Fatal(message string) {
	fmt.Println("FATAL: " + message)
}

func LogObject(object interface{}) {
	// do nothing
}
