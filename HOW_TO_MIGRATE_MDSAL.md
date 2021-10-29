## How to migrate GRA MD-SAL data between EOM and MCOM

Scripts to migrate data from SDN-NFT EOM to SDN-NFT MCOM and to export data
from MCOM to EOM are provided in ms/gra/gra-docker/src/main/scripts.

### Migrate MD-SAL data from EOM to MCOM
The importGraDaexim.sh script is used to import MD-SAL data
saved using the OpenDaylight daexim feature.  The data from this script
is loaded into the MCOM data via multiple "chunks", to work around limits
in file size on posts to the Azure environment.  The shell variable
CHUNK_SIZE can be used to adjust the number of entries included in each
chunk.  This variable defaults to 75.

The shell variables ODL_USER and ODL_PASSWORD must also be set to the
mechid / password that should be used for transactions to SDN-NFT.

The following are the steps to import MD-SAL data with the import script:
1. Use daexim in EOM to export MD-SAL data. 
2. Import exported data to MCOM using importGraDaexim.sh (in ms/gra/gra-docker/src/main/scripts)
    export ODL_USER=<mechid>
    export ODL_PASSWORD=<password for mechid>
    importGraDaexim.sh <export-file-name> <controller-url>


### Fallback MD-SAL data from MCOM to EOM
The graToMdsal.sh script is used to migrate data from the SDN-NFT MCOM
environment back to EOM. This script should be run in an environment
that can:
* Use kubectl to exec into the SDN-NFT GRA pod
* Use curl to send https POST commands to SDN-NFT EOM

This script uses the following environment variables:
* NFT_AZURE_USER: SDN-NFT mech id (default: SDN-NFT non-prod mech id)
* NFT_AZURE_PASSWORD:  SDN-NFT password (default:  SDN-NFT non-prod password)
* NFT_EOM_USER : SDN-NFT EOM user (default: admin)
* NFT_EOM_PASSWORD : SDN-NFT EOM password (default: admin)

To use this script to copy MD-SAL data from SDN-NFT MCOM data back to SDN-NFT EOM:

    graToMdsal.sh <nft-namespace> <gra pod name> <eom-url>



    




