//
//  MoviesState.swift
//  iosApp
//
//  Created by luu on 8/7/25.
//
import Foundation
import Shared

class MoviesState: ObservableObject {
    let viewModels: MoviesListViewModel
    init() {
        viewModels = MoviesListViewModel()
        
        viewModels.observeMovies { movies in
            print(movies)
        }
    }
    
    deinit {
        viewModels.dispose()
    }
}
