# Minepkl

A minecraft mod that integrates the [pkl](https://pkl-lang.org/) configuration
language into Minecraft. This mod is intented to be used in the context of modpack development by
modpack devs to generate minecraft datapacks and resourcepacks using pkl.

Current supported version is `1.20.1` on Forge and Fabric.

> Note: this mod is in early development and many features are missing,
> but a basic prototype is working. Expect bugs and many changes.

## Why Pkl?

**Pkl** is a dynamic language developed by Apple used to write scripts
that generate configuration files such as **JSON** or **YAML**. Pkl's
many features make it easier to write boring and repetitive configuration
files in simpler ways. For that reason, I decided to use Pkl to generate
datapacks for my modpack.

> Learn more about Pkl at https://pkl-lang.org/.

## Usage

> In this section, i'll assume you have a basic understanding on how to use pkl.

After installing, once you boot up minecraft, a directory called `pkl`
should be created in the root of your minecraft instance with 3 files:

- `build.pkl` - here is where you define the resourcepacks (data, assets) you want to generate;
- `external.pkl` - Can be used to generate files anywhere inside the
minecraft instance.

### Pack Definition

Inside of `minepkl/build.pkl`, define `packs` as a map where each *key*
is the file name of the pack. The pack object itself is defined with 3
properties:

```pkl
// All server resources are defined here (optional)
data: Mapping<String, Any>
// All client resources are defined here (optional)
assets: Mapping<String, Any>
// This is the description of the pack (optional)
description: String
```

Beware of the types. The generator requires the fields `data` and
`assets` to be a `Mapping` with `String` keys, where each key is a
`ResourceLocation` (e.g, `minecraft:recipes/my_recipe`). 

### Defining a new pack

Let's define a simple pack with a single recipe and translation:

```pkl
/// inside minepkl/build.pkl

myAssets {
  ["minecraft:lang/en_us"] {
    ["item.minecraft.apple"] = "Not an Apple!"
  }
}

myRecipes {
  ["minecraft:recipes/my_custom_recipe"] {
    type = "create:mixing"
    heatRequirement = "heated"

    ingredients {
      new { fluid = "minecraft:water" amount = 250 }
      new { item = "minecraft:oxidized_copper" }
    }

    results {
      new { item = "minecraft:copper_block" }
    }
  }
}

/// Define our packs
packs {
  ["Custom Datapack"] {
    data = myRecipes
    assets = myAssets
    description = "Simple Datapack"
  }
}
```

After running ``/reload`` and looking at `config/minepkl/generated`
we see there is a new file called ``Custom Datapack.zip``. If you
open it and look at it you'll see all the files you defined converted
to JSON.

### Generating External files

You can use ``minepkl/external.pkl`` to generated any file inside your minecraft
instance folder. For example:

```pkl
/// Inside minepkl/external.pkl

output {
  files {
    // External resources (eg, config files) can also be generated
    // Paths are relative to the minecraft instance and cannot be absolute
    ["config/my_config.yml"] {
      value {
        some_field = "A property"
        hello = "world"
        number = 2 * 3.14
      }

      // Define the output type of the file, in this case we use YAML
      renderer = new YamlRenderer {}
    }
  }
}
```

If you run ``/minepkl build external`` you'll see there is a file
``config/my_config.yml`` generated.

You may need to take a look at [here](https://pkl-lang.org/main/current/language-reference/index.html#module-output)
to learn how to use Module Outputs.

### Commands

- `/reload`: Use the normal `reload` command to automatically regenerate your packs.
- ``/minepkl build``: build all files (packs and external)
- ``/minepkl build <type>``: build a specific type (all, packs only or external files only)

