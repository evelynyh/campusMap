import React, {Component} from 'react';
import "./edge.css";

interface EdgeProps {

    onChange(lines: ColoredEdge[]): void;  // called when a new list of edges is ready

}
interface EdgeStates {
    //start value inputted in the text box
    s: string,

    //dest value inputted in the text box
    e: string,

    //building names
    b: string,
}

// how we store our path lines
interface ColoredEdge {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    color: string;
}

// path has the starting building, total cost, and
//list of path segments that will eventually become
// edges to be read
interface Path {
    cost: number,
    start: Point,

    path: Segment[],
}

//coordinates of beginning/ends or segments in our path
interface Point {
    x: number,
    y: number,
}

// short path segments of a path between 2 buildings
interface Segment {
    start: Point,
    end: Point,
    cost: number,
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class Edge extends Component<EdgeProps, EdgeStates> {
    constructor(props: any) {
        super(props);

        //we construct our text box to be empty with no lines drawn
        this.state = {
            s: "input starting building key name",
            e: "input destination building key name",
            b: "",
        };
    }

    //clears text box and stores the line values for later
    cleartext=() => {
        this.setState({
            s: "input starting building key name",
            e: "input destination building key name",
        });
        this.props.onChange([]);
    }

    //if the input contains values, draw the path
    drawing=async() => {
        try {
            // allows users to input building names in any case
            let st = this.state.s.toUpperCase()
            let dt = this.state.e.toUpperCase()

            let responsePromise = fetch("http://localhost:4567/draw-path?Start="+st+"&Dest="+dt);

            let response = await responsePromise;

            if (!response.ok) {
                // alert explains the possible user error to inform the client
                alert("your start or destination was invalid. Try looking at the building names and try again");
                return;
            }

            const textPromise = response.json();

            let path:Path = await textPromise as Path;

            // turns path into list of colored edges
            let c:ColoredEdge[] = []
            for(let i = 0; i < path.path.length; i++) {
                let start = path.path[i].start;
                let end = path.path[i].end;
                c.push({x1: start.x, y1: start.y, x2: end.x, y2: end.y, color:"purple"});
            }

            //gives this information out to be used elsewhere
            this.props.onChange(c);

        } catch (e) {
            // If an error/exception happens (such as if the fetch URL is wrong or the
            // server is offline), then we'll end up here. Probably best to show a message to the
            // user.
            alert("There was an error contacting the server.");
            console.log(e);  // Logging the error can be nice for debugging.
        }
    }

    //shows all building names in a popup
    names=async() => {
        try {
            let responsePromise = fetch("http://localhost:4567/buildings");

            // responsePromise is a Promise<Response>. We can get the value out of the promise by waiting
            // until the promise resolves:
            let response = await responsePromise;

            // Now that we have a response, we should check the status code, make sure it's OK=200:
            // because an error like this should not be user error, there isn't an alert comment that
            // will help the user understand the issue.
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return; // Don't keep trying to execute if the response is bad.
            }

            const textPromise = response.json();

            let l:Record<string,string> = await textPromise as Record<string,string>;

            //formats how the building names should be given to the user
            let result = "List of Buildings\n\n\n";
                result += "keys:                buildings:\n\n";
            for (let key of Object.keys(l)) {
                result +=  key;
                let ll = key.length;

                for (let ii = 0; ii <= (20 - ll); ii++) {
                    result += " ";
                }

                result += l[key] + "\n";
            }

            // shows the building names and associated key names
            this.setState({b: result});

        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);  // Logging the error can be nice for debugging.
        }
    }

    render() {
        // automatically have list of building names
        this.names()
        return (
            <div id="lines" className={"words"}>
                <div className={"text"}>
                    <br/>

                    {/*unmodifiable list of buildings*/}
                    <textarea
                        rows={30}
                        cols={80}
                        value = {this.state.b}
                    />

                    <br/>

                    {/*
                    sorry, but dropdowns look gross...
                    */}

                    {/*user types out starting point*/}
                    <textarea
                        rows={2}
                        cols={35}
                        onChange={(event) =>
                            this.setState({s: event.target.value})
                        }
                        value={this.state.s}
                    />


                    {/*user types out destination point*/}
                    <textarea
                        rows={2}
                        cols={35}
                        onChange={(event) =>
                            this.setState({e: event.target.value})
                        }
                        value={this.state.e}
                    />

                    <br/>
                    <br/>
                </div>

                <div className={"b"}>
                        {/*draws path between buildings when pressed*/}
                    <button onClick={this.drawing}>Make Path</button>

                        {/*clears paths and text box when pressed*/}
                    <button onClick={this.cleartext}>Clear</button>
                </div>

            </div>
        );
    }
}

export default Edge;