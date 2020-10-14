#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 uv;
layout (location = 3) in float textureId;

out vec4 shader_color;
out vec2 tex_coord;
out float tex_idx;

void main()
{
    gl_Position = vec4(aPos, 1.0);
    shader_color = vec4(color, 1.0);
    tex_coord = uv;
    tex_idx = textureId;
}

    #---
    #version 330 core

in vec4 shader_color;
in vec2 tex_coord;
in float tex_idx;

uniform sampler2D textureSampler[2];

out vec4 FragColor;

void main()
{
    FragColor = texture(textureSampler[int(tex_idx)], tex_coord);
//    FragColor = shader_color;
}
