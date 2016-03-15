$input v_color0, v_texcoord0

// license:BSD-3-Clause
// copyright-holders:Ryan Holtz,ImJezze
//-----------------------------------------------------------------------------
// Defocus Effect
//-----------------------------------------------------------------------------

#include "../../../../../3rdparty/bgfx/examples/common/common.sh"

uniform vec4 u_screenrect;

uniform vec4 u_defocus;

uniform vec4 u_swap_xy;
uniform vec4 u_screen_dims;

SAMPLER2D(DiffuseSampler, 0);

//-----------------------------------------------------------------------------
// Constants
//-----------------------------------------------------------------------------

void main()
{
	const vec2 Coord1Offset = vec2( 0.75,  0.50);
	const vec2 Coord2Offset = vec2( 0.25,  1.00);
	const vec2 Coord3Offset = vec2(-0.50,  0.75);
	const vec2 Coord4Offset = vec2(-1.00,  0.25);
	const vec2 Coord5Offset = vec2(-0.75, -0.50);
	const vec2 Coord6Offset = vec2(-0.25, -1.00);
	const vec2 Coord7Offset = vec2( 0.50, -0.75);
	const vec2 Coord8Offset = vec2( 1.00, -0.25);

	vec2 QuadRatio = vec2(1.0, (u_swap_xy.x > 0.0) ? u_screen_dims.y / u_screen_dims.x : u_screen_dims.x / u_screen_dims.y);

	vec2 DefocusTexelDims = u_defocus.xy / u_screen_dims.xy;
	
	vec4 d0 = texture2D(DiffuseSampler, v_texcoord0);
	vec4 d1 = texture2D(DiffuseSampler, v_texcoord0 + Coord1Offset * DefocusTexelDims);
	vec4 d2 = texture2D(DiffuseSampler, v_texcoord0 + Coord2Offset * DefocusTexelDims);
	vec4 d3 = texture2D(DiffuseSampler, v_texcoord0 + Coord3Offset * DefocusTexelDims);
	vec4 d4 = texture2D(DiffuseSampler, v_texcoord0 + Coord4Offset * DefocusTexelDims);
	vec4 d5 = texture2D(DiffuseSampler, v_texcoord0 + Coord5Offset * DefocusTexelDims);
	vec4 d6 = texture2D(DiffuseSampler, v_texcoord0 + Coord6Offset * DefocusTexelDims);
	vec4 d7 = texture2D(DiffuseSampler, v_texcoord0 + Coord7Offset * DefocusTexelDims);
	vec4 d8 = texture2D(DiffuseSampler, v_texcoord0 + Coord8Offset * DefocusTexelDims);

	vec4 blurred = (d0 + d1 + d2 + d3 + d4 + d5 + d6 + d7 + d8) / 9.0;

	gl_FragColor = vec4(blurred.xyz, 1.0) * v_color0;
}
