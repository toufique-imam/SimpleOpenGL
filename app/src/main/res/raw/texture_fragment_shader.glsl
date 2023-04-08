precision mediump float;

uniform sampler2D u_TextureUnit1;
uniform sampler2D u_TextureUnit2;
uniform vec4 u_ShaderColor;
varying vec2 v_TextureCoordinates;

void main() {
    vec4 texColor1 =  texture2D(u_TextureUnit1, v_TextureCoordinates);
    vec4 texColor2 =  texture2D(u_TextureUnit2, v_TextureCoordinates);
    vec4 texColor = texColor1;
    gl_FragColor = u_ShaderColor * texColor;
}