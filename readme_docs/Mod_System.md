# Mod System

The Mod System in TGL was inspired by Gamebryo and Creation Engine's mod/content loading system. TGL aspires to improve, fix and expand on the concept. 

TGL Mods come in files called GLArchives or Gilded Library Archive files. They're a custom file format for the engine, the format is documented and easy to parse, using ZStd for compression. GLArchives come in 3 flavors:
    * GLAssetArchive - Holds only files.
    * GLModArchive - Holds only records.
    * GLBaseArchive - A combination of both Asset and Mod Archives, holding both records and assets. 

You can think of GLAssetArchives, or `.gla` files, as an analogue to the Bethesda Softworks Archive, or `.bsa`. GLBaseArchives are an analogue to Elder Scroll Master files, or `.esm`, and GLBaseArchives can function as both the Elder Scrolls Plugin and Elder Scrolls Light Masters, or `.esp` and `.esl` files. 

GLBaseArchives usually are used for Games, DLC and bigger mods. 
GLModArchives usually are used for content mods or patches. 

Games, DLC, DLC Sized Mods, Mods, and Patches made with TGL will from here on be henceforth known as just "Mods". 

Mods in TGL have a priority, lists of dependencies, optional dependencies and conflicts. There is also a tag system, that has been adopted from LOOT and implemented in the Editor. These tags help with grouping and sorting. 

