import React from "react";
import belle from 'belle';

export default class InputForm extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        const point = {
            'x': event.target.x,
            'y': event.target.y,
            'r': event.target.r
        };
        this.props.addPoint(point);
        event.target.reset();
    }

    render() {
        const TextInput = belle.TextInput;
        return <form id="inputForm" className="inputForm" onSubmit={this.handleSubmit}>
            <label htmlFor='x' className='label'>X:</label>
            <TextInput id='x' value={this.state.x} onUpdate={(o) => {
                this.setState({'x': o.value})
            }}/>
            <label htmlFor='y' className='label'>Y:</label>
            <TextInput id='y' value={this.state.y} onUpdate={(o) => {
                this.setState({'y': o.value})
            }}/>
            <label htmlFor='r' className='label'>R:</label>
            <TextInput id='r' value={this.state.r} onUpdate={(o) => {
                this.setState({'r': o.value})
            }}/>
        </form>
    }
}