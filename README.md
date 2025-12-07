# Minepkl

A minecraft mod that integrates the [pkl](https://pkl-lang.org/) configuration
language into Minecraft. This mod is intented to be used in the context of modpack development by
modpack devs to generate minecraft datapacks and resourcepacks using pkl.

Current supported version is `1.20.1` on Forge and Fabric.

Needs [Moonlight Lib](https://modrinth.com/mod/moonlight) as a dependency.

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

- `data.pkl` - for server resources, such as recipes, tags, etc;
- `asset.pkl` - for client resources, such as lang files;
- `external.pkl` - Can be used to generate files anywhere inside the
minecraft instance.

### Generating assets

Inside of `pkl/asset.pkl` you can define any asset that should
be added to the output resourcepack. For example:

```pkl
assets {
  ["mymod:lang/en_us"] {
    ["item.mymod.some_item"] = "My custom item name"
    ["item.mymod.another_item"] = "I don't know anymore"
  }
}

output {
  files {
    for (location, contents in assets) {
      [location] {
        value = contents
        renderer = new JsonRenderer {}
      }
    }
  }
}
```

- We define an `assets` map with one field `mymod:lang/en_us`, which is the
path (minecraft resource location notation) of the output file and the
value of that file (will be converted to JSON).

- Then, using the pkl `output.files` property of a module, we define an output
file for each key-value pair inside `assets`.

- We use `JsonRenderer` since minecraft expects a JSON file.

In this case, the output file will be located at
`<generated_pack>/assets/mymod/lang/en_us.json` with the following contents:

```json
{
  "item.mymod.some_item": "My custom item name",
  "item.mymod.another_item": "I don't know anymore"
}
```

### Generating data

Inside of `pkl/data.pkl` you can define any files that should be
added to the generated datapack. For example, with a simple *create* recipe:

```pkl
data {
  ["my_mod:recipes/my_custom_recipe"] {
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

output {
  files {
    for (location, contents in data) {
      [location] {
        value = contents
        renderer = new JsonRenderer {}
      }
    }
  }
}
```

The output file will be located at `<generated_pack>/data/my_mod/recipes/my_custom_recipe.json`
and the contents will be:

```json
{
  "type": "create:mixing",
  "heatRequirement": "heated",
  
  "ingredients": [
    { "fluid": "minecraft:water", "amount": 250 },
    { "item": "minecraft:oxidized_copper" }
  ],
  
  "results": [
    { "item": "minecraft:copper_block" }
  ]
}
```
