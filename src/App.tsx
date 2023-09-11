/*
 * Copyright (C) 2022 Kevin Zatloukal and James Wilcox.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";
import Edge from "./Edge";
import Map from "./Map";

interface AppState {
    request: ColoredEdge[];
}

interface ColoredEdge {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    color: string;
}

class App extends Component<{}, AppState> {


    constructor(props: {}) {
        super(props);

        //path information to be passed to the map
        this.state = {
            request: [],
        };
    }

    render() {


        return (
            <div>
                <div className={"header"} >
                <h1 id="app-title">Campus Paths</h1>
                </div>

                <div className={"next"}>
                    <Edge
                        onChange={(lines) => {
                            this.setState({request: lines});
                        }}
                    />

                    <div>
                        <Map
                            lines = {this.state.request}
                        />
                    </div>
                </div>
            </div>
        );
    }

}

export default App;
