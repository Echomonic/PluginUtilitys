package dev.echo.utils.general;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class Region {

    @Getter
    @Setter
    private Location min,max;
    private String world;
    private Vector minV, maxV;

    public Region(Location min, Location max) {
        this.min = min;
        this.max = max;

        double xPos1 = Math.min(min.getX(), max.getX());
        double yPos1 = Math.min(min.getY(), max.getY());
        double zPos1 = Math.min(min.getZ(), max.getZ());
        double xPos2 = Math.max(min.getX(), max.getX());
        double yPos2 = Math.max(min.getY(), max.getY());
        double zPos2 = Math.max(min.getZ(), max.getZ());

        minV = new Vector(xPos1,yPos1,zPos1);
        maxV = new Vector(xPos2,yPos2,zPos2);
    }
    public Region() {
    }
    public boolean containsLocation(Location location){

        return location.toVector().isInAABB(minV, maxV);
    }
    public List<Block> getBlocks(){
        int minX,maxX,minY,maxY,minZ,maxZ;

        minX = min.getBlockX();
        minY = min.getBlockY();
        minZ = min.getBlockZ();
        maxX = max.getBlockX();
        maxY = max.getBlockY();
        maxZ = max.getBlockZ();

        List<Block> blocks = Lists.newArrayList();

        for(int x = maxX - minX; x <= maxX + minX; x++){
            for(int y = maxY - minY; y <= maxY + minY; y++){
                for(int z = maxX - minZ; z <= maxZ + minZ; z++){
                    Block block = max.getWorld().getBlockAt(x,y,z);
                    if(block.getType() != Material.AIR){
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }
    public boolean isInLocation(Player player){
        return containsLocation(player.getLocation());
    }
    public boolean blockInLocation(Block block){

        return containsLocation(block.getLocation());
    }

}
