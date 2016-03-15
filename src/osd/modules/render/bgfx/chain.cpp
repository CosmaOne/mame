// license:BSD-3-Clause
// copyright-holders:Ryan Holtz
//============================================================
//
//  chain.cpp - BGFX screen-space post-effect chain
//
//============================================================

#include "emu.h"

#include <bx/timer.h>

#include "slider.h"
#include "parameter.h"
#include "entryuniform.h"
#include "texturemanager.h"
#include "vertex.h"

#include "chain.h"

bgfx_chain::bgfx_chain(std::string name, std::string author, std::vector<bgfx_slider*> sliders, std::vector<bgfx_parameter*> params, std::vector<bgfx_chain_entry*> entries, std::string output)
	: m_name(name)
	, m_author(author)
	, m_sliders(sliders)
	, m_params(params)
	, m_entries(entries)
    , m_output(output)
    , m_current_time(0)
{
    for (bgfx_slider* slider : m_sliders)
    {
        m_slider_map[slider->name()] = slider;
    }
}

bgfx_chain::~bgfx_chain()
{
	for (bgfx_slider* slider : m_sliders)
	{
		delete slider;
	}
	for (bgfx_parameter* param : m_params)
	{
		delete param;
	}
	for (bgfx_chain_entry* entry : m_entries)
	{
		delete entry;
	}
}

void bgfx_chain::process(render_primitive* prim, int view, texture_manager& textures, uint16_t screen_width, uint16_t screen_height, uint64_t blend)
{
    int current_view = view;
    for (bgfx_chain_entry* entry : m_entries)
	{
        if (!entry->skip())
        {
            entry->submit(prim, current_view, textures, screen_width, screen_height, blend);
            current_view++;
        }
	}

    m_current_time = bx::getHPCounter();
    static int64_t last = m_current_time;
    const int64_t frameTime = m_current_time - last;
    last = m_current_time;
    const double freq = double(bx::getHPFrequency());
    const double toMs = 1000.0 / freq;
    const double frameTimeInSeconds = (double)frameTime / 1000000.0;

    for (bgfx_parameter* param : m_params)
    {
        param->tick(frameTimeInSeconds* toMs);
    }
}

uint32_t bgfx_chain::applicable_passes()
{
    int applicable_passes = 0;
    for (bgfx_chain_entry* entry : m_entries)
	{
        if (!entry->skip())
        {
            applicable_passes++;
        }
    }

    return applicable_passes;
}