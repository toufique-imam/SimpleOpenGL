precision mediump float;
varying vec4 v_Color; // a uniform keeps the same value for all vertices until we change it again

void main() {
    gl_FragColor = v_Color;
}