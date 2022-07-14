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

import (
	"errors"
	"io/ioutil"
	"mime"
	"os"
	"path/filepath"
)

type FileAsset struct {
	Id             string `json:"id"`        // the unique ID for this file
	Name           string `json:"name"`      // name of this file
	Path           string `json:"path"`      // path of this file
	Extension      string `json:"extension"` // extension extracted for this file
	MimeType       string `json:"mimeType"`  // mime type base on extension of this file
	IsFolder       bool   `json:"isFolder"`  // whether the file is a folder or not
	IsSymbolicLink bool   `json:"isSymLink"` // whether the file is a symlink or not
	Size           uint64 `json:"size"`      // size of file
	Created        int64  `json:"created"`   // when was the file created
	Modified       int64  `json:"modified"`  // when was the file last modified
}

type FileFilter func(asset FileAsset) bool

var folderFilter = func(asset FileAsset) bool {
	return asset.IsFolder
}

var fileFilter = func(asset FileAsset) bool {
	return !asset.IsFolder
}

func ListChildFolders(path string) ([]*FileAsset, error) {
	return listFilesInternal(path, false, folderFilter)
}

func ListFiles(path string, recursive bool) ([]*FileAsset, error) {
	return listFilesInternal(path, recursive, nil)
}

func ListFilesExcludingFolders(path string, recursive bool) ([]*FileAsset, error) {
	return listFilesInternal(path, recursive, fileFilter)
}

// internal method that allows us to read files
func listFilesInternal(path string, recursive bool, filter FileFilter) ([]*FileAsset, error) {
	var err error

	// basic valid checks
	if path == "" {
		return nil, errors.New("Path for dir list cannot be empty")
	}

	// read files from the path
	files, err := ioutil.ReadDir(path)
	if err != nil {
		return nil, err
	}

	// create a dynamic array
	fileLength := len(files)
	var assetList []*FileAsset
	if filter == nil {
		assetList = make([]*FileAsset, fileLength)
	} else {
		assetList = make([]*FileAsset, 0, fileLength)
	}

	// populate the array
	for index, file := range files {
		extension := filepath.Ext(file.Name())

		asset := FileAsset{
			Id:             filepath.Join(path, file.Name()),
			Name:           file.Name(),
			Extension:      extension,
			Path:           path,
			Size:           uint64(file.Size()),
			IsFolder:       file.IsDir(),
			Modified:       file.ModTime().Unix(),
			IsSymbolicLink: file.Mode()&os.ModeSymlink == os.ModeSymlink,
			MimeType:       mime.TypeByExtension(extension),
		}

		// check for filter
		if !recursive && filter == nil {
			assetList[index] = &asset
		} else {
			// check if we have a filter
			includeAsset := true
			if filter != nil {
				includeAsset = filter(asset)
			}

			if includeAsset {
				assetList = append(assetList, &asset)
			}
		}

		// is this recursive mode?
		if recursive && asset.IsFolder {
			childAssets, err := listFilesInternal(filepath.Join(path, asset.Name), recursive, filter)
			if err != nil {
				return nil, err
			}

			assetList = append(assetList, childAssets...)
		}
	}

	// return the list of files
	return assetList, nil
}
